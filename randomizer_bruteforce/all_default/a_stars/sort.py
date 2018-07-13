import re

# Sort by the amount of stars

seeds = []
for match in re.finditer(r"(\d+) \((\d+)\)", open("output.txt").read()):
    # Got halfway through and realized the amount of seeds with 20 so I changed the min,
    #  but they'd still be in the output
    if int(match.group(2)) > 20:
        seeds.append((int(match.group(1)), int(match.group(2))))

seeds = sorted(seeds, key=lambda x: x[1])[::-1]

open("sorted.txt", "w").write("\n".join(["{} ({})".format(x[0], x[1]) for x in seeds]))
