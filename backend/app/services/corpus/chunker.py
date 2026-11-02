"""Cargador y segmentador del corpus clínico documental (corpus_habitar)."""

import os
import re
import glob
from typing import List, Dict, Any, Tuple

MIN_PALABRAS = 25
MAX_PALABRAS = 220


def words(s: str) -> int:
    return len(re.findall(r"\w+", s))


def parse_doc(path: str) -> Tuple[Dict[str, str], str]:
    with open(path, "r", encoding="utf-8") as f:
        txt = f.read()

    m = re.match(r"(?s)^---\n(.*?)\n---\n(.*)$", txt)
    fm, body = (m.group(1), m.group(2)) if m else ("", txt)

    meta = {}
    for line in fm.splitlines():
        if ":" in line:
            k, v = line.split(":", 1)
            meta[k.strip()] = v.strip()

    return meta, body


def split_sections(body: str) -> Tuple[str, List[Tuple[str, str]]]:
    title_match = re.search(r"(?m)^# (.+)$", body)
    title = title_match.group(1) if title_match else ""

    parts = re.split(r"(?m)^## ", body)
    intro = parts[0]
    secs: List[Tuple[str, str]] = []

    intro_txt = re.sub(r"(?m)^# .+$", "", intro).strip()
    if intro_txt:
        secs.append((title, intro_txt))

    for p in parts[1:]:
        head, _, rest = p.partition("\n")
        secs.append((head.strip(), rest.strip()))

    return title, secs


def chunk_corpus(folder: str) -> List[Dict[str, Any]]:
    """Segmenta los archivos doc*.md del corpus en fragmentos estructurados con trazabilidad."""
    chunks: List[Dict[str, Any]] = []
    pattern = os.path.join(folder, "doc*.md")

    for path in sorted(glob.glob(pattern)):
        meta, body = parse_doc(path)
        doc_title, secs = split_sections(body)

        merged: List[Tuple[str, str]] = []
        for head, text in secs:
            if merged and words(merged[-1][1]) < MIN_PALABRAS:
                h0, t0 = merged.pop()
                merged.append((h0, t0 + "\n\n## " + head + "\n" + text))
            else:
                merged.append((head, text))

        if len(merged) > 1 and words(merged[-1][1]) < MIN_PALABRAS:
            h0, t0 = merged.pop()
            h1, t1 = merged.pop()
            merged.append((h1, t1 + "\n\n## " + h0 + "\n" + t0))

        n = 0
        for head, text in merged:
            paras = [p for p in text.split("\n") if p.strip()]
            buf: List[str] = []
            cur = 0
            groups: List[List[str]] = []

            for p in paras:
                w = words(p)
                if cur + w > MAX_PALABRAS and buf:
                    groups.append(buf)
                    buf, cur = [], 0
                buf.append(p)
                cur += w
            if buf:
                groups.append(buf)

            for g in groups:
                n += 1
                body_txt = "\n".join(g)
                chunk_id = f"{meta.get('id', '?')}#{n}"
                chunks.append({
                    "chunk_id": chunk_id,
                    "doc_id": meta.get("id", "?"),
                    "doc_titulo": meta.get("titulo", doc_title),
                    "seccion": head,
                    "origen": meta.get("origen", ""),
                    "version": meta.get("version", ""),
                    "texto": f"{doc_title} — {head}\n{body_txt}",
                    "palabras": words(body_txt),
                })

    return chunks
