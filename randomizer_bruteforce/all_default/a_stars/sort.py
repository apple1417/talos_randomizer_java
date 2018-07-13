import re

seeds = []
for match in re.finditer(r"(\d+) \((\d+)\)", open("output.txt").read()):
    seeds.append((int(match.group(1)), int(match.group(2))))

seeds = sorted(seeds, key=lambda x: x[1])[::-1]

open("sorted.txt", "w").write("\n".join(["{} ({})".format(x[0], x[1]) for x in seeds]))
