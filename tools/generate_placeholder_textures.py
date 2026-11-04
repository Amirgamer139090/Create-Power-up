#!/usr/bin/env python3
"""Generates placeholder PNG textures for the items and blocks of Create: Power up!.

    python tools/generate_placeholder_textures.py

Python 3, standard library only. Output is deterministic (same input, byte-identical PNGs).

The material list is read from PowerMaterial.java. The constant block is parsed strictly: every entry
between the opening brace and the terminating semicolon must look like NAME(Category.X), otherwise the
script stops with an error instead of silently skipping an entry. The number of parsed entries is also
cross-checked against the number of "(Category." occurrences in the code (comments ignored).

The script overwrites existing textures of the same name; do not re-run it after replacing a
placeholder with real art unless you intend to.
"""
import re
import struct
import sys
import zlib
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
ENUM_FILE = ROOT / "src/main/java/com/powerup/materials/PowerMaterial.java"
TEXTURES = ROOT / "src/main/resources/assets/create_powerup/textures"

CATEGORY_COLORS = {
    "METAL": (205, 127, 50),
    "SEMICONDUCTOR": (110, 120, 150),
    "OPTICAL": (150, 215, 235),
    "POLYMER": (235, 235, 225),
    "ELECTRONIC": (40, 150, 80),
    "CHEMICAL": (120, 100, 140),
    "THERMAL": (215, 190, 160),
}

ENTRY = re.compile(r"([A-Z][A-Z0-9_]*)\(Category\.([A-Z_]+)\)")


def parse_materials(text):
    start = text.index("public enum PowerMaterial {") + len("public enum PowerMaterial {")
    end = text.index(";", start)  # first semicolon ends the constant list
    block = re.sub(r"//[^\n]*", "", text[start:end])
    entries = []
    for token in block.split(","):
        token = token.strip()
        if not token:
            raise ValueError("empty entry (stray or trailing comma) in PowerMaterial constants")
        match = ENTRY.fullmatch(token)
        if match is None:
            raise ValueError("cannot parse PowerMaterial constant: %r" % token)
        name, category = match.groups()
        if category not in CATEGORY_COLORS:
            raise ValueError("%s uses unknown category %s" % (name, category))
        entries.append((name, category))
    names = [n for n, _ in entries]
    if len(set(names)) != len(names):
        raise ValueError("duplicate material names")
    code = re.sub(r"/\*.*?\*/", "", text, flags=re.S)  # ignore comments (Javadoc mentions the syntax)
    code = re.sub(r"//[^\n]*", "", code)
    if code.count("(Category.") != len(entries):
        raise ValueError("parsed %d materials but code contains %d '(Category.' occurrences"
                         % (len(entries), code.count("(Category.")))
    return entries


def png_bytes(pixels):
    height, width = len(pixels), len(pixels[0])
    raw = bytearray()
    for row in pixels:
        raw.append(0)  # filter type: none
        for r, g, b, a in row:
            raw += bytes((r, g, b, a))

    def chunk(tag, data):
        body = tag + data
        return struct.pack(">I", len(data)) + body + struct.pack(">I", zlib.crc32(body) & 0xFFFFFFFF)

    return (b"\x89PNG\r\n\x1a\n"
            + chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0))
            + chunk(b"IDAT", zlib.compress(bytes(raw), 9))
            + chunk(b"IEND", b""))


def shade(color, delta):
    return tuple(max(0, min(255, v + delta)) for v in color)


def item_pixels(color):
    pixels = [[(0, 0, 0, 0)] * 16 for _ in range(16)]
    for y in range(2, 14):
        for x in range(2, 14):
            if x in (2, 13) or y in (2, 13):
                c = shade(color, -70)
            elif x <= 3 or y <= 3:
                c = shade(color, 35)
            else:
                c = color
            pixels[y][x] = (*c, 255)
    return pixels


def ore_pixels():
    state = 1234
    pixels = []
    for _ in range(16):
        row = []
        for _ in range(16):
            state = (state * 1103515245 + 12345) & 0x7FFFFFFF  # deterministic LCG
            v = 118 + (state >> 16) % 25
            row.append((v, v, v, 255))
        pixels.append(row)
    for cx, cy in [(3, 4), (9, 3), (5, 10), (12, 9), (8, 13)]:
        for dx, dy in [(0, 0), (1, 0), (0, 1), (1, 1)]:
            pixels[cy + dy][cx + dx] = (235, 232, 225, 255)
    return pixels


def write(path, pixels):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(png_bytes(pixels))
    print("wrote", path.relative_to(ROOT))


def main():
    try:
        materials = parse_materials(ENUM_FILE.read_text(encoding="utf-8"))
    except ValueError as error:
        print("error:", error, file=sys.stderr)
        return 1
    for index, (name, category) in enumerate(materials):
        color = shade(CATEGORY_COLORS[category], (index * 23) % 41 - 20)
        write(TEXTURES / "item" / (name.lower() + ".png"), item_pixels(color))
    write(TEXTURES / "block" / "quartz_ore.png", ore_pixels())
    return 0


if __name__ == "__main__":
    sys.exit(main())
