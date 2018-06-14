"""
    This is very specialized, I cut a lot of code for other options/modes
    One-hub F6-seeds:
      4705990
      20646763
      21579776
      32632976
    Future seed, when F3 star is fixed:
      178957
"""
import re
import sys
base_seed = 0
max_seed = 0x7FFFFFFF
try:
    base_seed = int(sys.argv[1])
    max_seed = max(base_seed, min(0x7FFFFFFF, int(sys.argv[2])))
except:
    pass

def SetVarValue(var, val):
    talosProgress[var] = val
def SetVar(var):
    SetVarValue(var, 1)
def GetVarValue(var):
    return talosProgress[var] if var in talosProgress.keys() else -1
def IsVarSet(var):
    return GetVarValue(var) != -1
SetCodeValue = SetVarValue
SetCode = SetVar
GetCodeValue = GetVarValue
IsCodeSet = IsVarSet

def isWorldOpen(world):
    if world == "A1": return True
    if randomization_mode == "Intended" and locked["A1 Gate"] != False: return False
    if (scavenger + loop) != 0: return True

    starOverride = True
    num = GetCodeValue(world)
    if num == 8 or num == 17 or num == 25:
      starOverride = not (locked["A Star"] or locked["B Star"] or locked["C Star"])

    return starOverride and world in open_worlds

def addWorldMarkers(worlds):
    global closedMarkers
    global open_worlds
    if not worlds: return None
    for i in range(len(worlds)):
        closedMarkers.append(worlds[i])
        open_worlds.append(markers[worlds[i]][2])
    return None

def placeSigils(sigils):
    global markers
    global openMarkers
    global availableMarkers
    for i in sigils:
        index = rand(0, availableMarkers - 1)
        for j in range(len(openMarkers)):
            if index >= len(markers[openMarkers[j]][1]):
                index = index - len(markers[openMarkers[j]][1])
            else:
                randMarker = markers[openMarkers[j]][1].pop(index)
                SetCodeValue(translate[randMarker], i)
                availableMarkers = availableMarkers - 1
                if len(markers[openMarkers[j]][1]) == 0:
                    openMarkers.pop(j)
                break

allMarkers = [
    "001_SPM_000_PM_005", "001a_SPM_000_PM_008", "005_SPM_000_PM_009", "006_SPM_000_PM_003",
    "007_SPM_000_PM_006", "008_SPM_000_PM_016", "011_SPM_000_PM_009", "012_SPM_000_PM_004",
    "013_SPM_000_PM_006", "015_SRT_SPM_000_PM_017", "015_SRT_SPM_000_PM_018", "017_SPM_000_PM_023",
    "020_SPM_000_PM_014", "107_SPM_000_PM_007", "108_SPM_000_PM_012", "111_SPM_000_PM_012",
    "112_SPM_000_PM_034", "113_SPM_000_PM_036", "114_SPM_000_PM_032", "115_SRT_TAM_004_PM_016",
    "117_SRT_SPM_000_PM_028", "118_SPM_000_PM_062", "119_SRT_SPM_000_PM_033", "120_SPM_000_PM_029",
    "201_SPM_000_PM_013", "201_SRT_SPM_000_PM_004", "202b_SPM_000_PM_004", "202c_SPM_000_PM_003",
    "202d_SPM_000_PM_002", "202e_SPM_000_PM_004", "202f_SPM_000_PM_003", "203_SPM_000_PM_011",
    "204_SPM_000_PM_004", "205_SPM_000_PM_003", "206_SPM_000_PM_021", "207_SPM_000_PM_005",
    "208_SPM_000_PM_014", "209_SPM_000_PM_012", "210_SPM_000_PM_015", "211_SPM_000_PM_008",
    "212_SPM_000_PM_017", "213_SPM_000_PM_010", "214_SRT_SPM_000_PM_025", "215_SPM_000_PM_013",
    "216_SPM_000_PM_015", "217_SPM_000_PM_040", "218_SPM_000_PM_016", "219_SPM_000_PM_008",
    "220_SPM_000_PM_032", "221_SPM_002_PM_001", "222_SPM_004_PM_001", "223_SPM_000_PM_009",
    "224_SRT_SPM_000_PM_071", "224_SRT_SPM_000_PM_091", "225_SPM_000_PM_044", "226_SPM_000_PM_039",
    "227_SPM_002_PM_033", "229_SPM_000_PM_070", "230_SPM_000_PM_019", "232_SPM_000_PM_012",
    "233_SPM_000_PM_015", "234_SPM_000_PM_015", "235_SRT_SPM_000_PM_037", "238_SPM_000_PM_018",
    "239_SPM_000_PM_018", "244_SPM_000_PM_008", "244_SRT_SPM_000_PM_006", "300a_SPM_000_PM_007",
    "301_SPM_000_PM_010", "302_SPM_000_PM_008", "303_SPM_000_PM_010", "305_SPM_000_PM_004",
    "306_SRT_SPM_000_PM_016", "308_SPM_000_PM_017", "309_SPM_000_PM_018", "310_SPM_000_PM_024",
    "311_SPM_000_PM_041", "312_SPM_000_PM_032", "313_SPM_000_PM_016", "314_SPM_000_PM_012",
    "315_TAM_002_PM_001", "316_SPM_000_PM_014", "317_SPM_000_PM_024", "318_SPM_000_PM_026",
    "319_SPM_000_PM_008", "320_SRT_SPM_000_PM_046", "321_SPM_000_PM_005", "322_SPM_000_PM_008",
    "328_SPM_000_PM_016", "401_SPM_004_PM_008", "402_SPM_000_PM_020", "403_SPM_000_PM_015",
    "404_SPM_000_PM_022", "405_SRT_SPM_000_PM_047", "405_SRT_SPM_000_PM_050", "407_SPM_000_PM_018",
    "408_SPM_000_PM_033", "408_SRT_SPM_000_PM_034", "409_SPM_000_PM_024", "411_SRT_SPM_000_PM_014",
    "414_SPM_000_PM_007", "416_SPM_000_PM_026", "417_SPM_000_PM_029", "418_SPM_000_PM_014",
    "504_SRT_SPM_000_PM_021", "Cloud_1_02_SRT_SPM_000_PM_016", "Cloud_1_02_SRT_SPM_001_PM_003", "Cloud_1_03_SRT_SPM_000_PM_005",
    "Cloud_1_04_SRT_SPM_000_PM_007", "Cloud_1_06_SRT_SPM_000_PM_007", "Cloud_1_07_SRT_SPM_000_PM_021", "Cloud_2_01_SRT_SPM_000_PM_004",
    "Cloud_2_02_SRT_SPM_000_PM_039", "Cloud_2_03_SRT_SPM_002_PM_013", "Cloud_2_04_SRT_SPM_000_PM_017", "Cloud_2_04_SRT_SPM_002_PM_002",
    "Cloud_2_05_SRT_TAM_003_PM_003", "Cloud_2_07_SRT_TAM_001_PM_004", "Cloud_3_01_SRT_SPM_000_PM_017", "Cloud_3_02_SRT_TAM_001",
    "Cloud_3_03_SRT_SPM_000_PM_069", "Cloud_3_05_SRT_SPM_000_PM_035", "Cloud_3_05_SRT_SPM_002_PM_016", "Cloud_3_05_SRT_SPM_003_PM_012",
    "Cloud_3_06_SRT_SPM_000_PM_008", "Cloud_3_07_SRT_SPM_000_PM_021", "Islands_01_SRT_SPM_000_PM_003", "LeapOfFaith_PM_010",
    "Secret_28_SRT_SPM_000_PM_004",
    "PaintItemSeed", "Code_Floor4", "Code_Floor5", "Code_Floor6",
    "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "ADevIsland",
    "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8",
    "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "CMessenger",
    "Randomizer_Seed", "Randomizer_Mode", "Randomizer_Scavenger", "Randomizer_Loop"
]
def checksum():
    sum1 = 0
    sum2 = 0
    for index in range(len(allMarkers)):
        value = GetCodeValue(allMarkers[index])
        if value == -1:
            print("'" + allMarkers[index] + "' does not have a value assigned to it")
        sum1 = (sum1 + value*(index + 1)) % 65536
        sum2 = (sum2 + sum1) % 65536
    return "%08X" % (sum1 * 65536 + sum2)

translate = {
    "A1-PaSL": "005_SPM_000_PM_009", "A1-Beaten Path": "107_SPM_000_PM_007", "A1-Outnumbered": "006_SPM_000_PM_003", "A1-OtToU": "011_SPM_000_PM_009", "A1-ASooR": "007_SPM_000_PM_006", "A1-Trio": "012_SPM_000_PM_004", "A1-Peephole": "001_SPM_000_PM_005", "A1-Star": "Cloud_1_02_SRT_SPM_000_PM_016",
    "A2-Guards": "008_SPM_000_PM_016", "A2-Hall of Windows": "001a_SPM_000_PM_008", "A2-Suicide Mission": "013_SPM_000_PM_006", "A2-Star": "Cloud_1_02_SRT_SPM_001_PM_003",
    "A3-Stashed for Later": "108_SPM_000_PM_012", "A3-ABTU": "015_SRT_SPM_000_PM_018", "A3-ABTU Star": "015_SRT_SPM_000_PM_017", "A3-Swallowed the Key": "020_SPM_000_PM_014", "A3-AEP": "017_SPM_000_PM_023", "A3-Clock Star": "Cloud_1_03_SRT_SPM_000_PM_005",
    "A4-Branch it Out": "202c_SPM_000_PM_003", "A4-Above All That": "202f_SPM_000_PM_003", "A4-Push it Further": "202b_SPM_000_PM_004", "A4-Star": "Cloud_1_04_SRT_SPM_000_PM_007", "A4-DCtS": "202d_SPM_000_PM_002",
    "A5-Two Boxes": "201_SPM_000_PM_013", "A5-Two Boxes Star": "201_SRT_SPM_000_PM_004", "A5-YKYMCTS": "204_SPM_000_PM_004", "A5-Over the Fence": "202e_SPM_000_PM_004", "A5-OLB": "207_SPM_000_PM_005", "A5-FC": "244_SPM_000_PM_008", "A5-FC Star": "244_SRT_SPM_000_PM_006",
    "A6-Mobile Mindfield": "111_SPM_000_PM_012", "A6-Deception": "210_SPM_000_PM_015", "A6-Door too Far": "218_SPM_000_PM_016", "A6-Bichromatic": "303_SPM_000_PM_010", "A6-Star": "Cloud_1_06_SRT_SPM_000_PM_007",
    "A7-LFI": "212_SPM_000_PM_017", "A7-Trapped Inside": "305_SPM_000_PM_004", "A7-Two Buzzers": "209_SPM_000_PM_012", "A7-Star": "Cloud_1_07_SRT_SPM_000_PM_021", "A7-WiaL": "220_SPM_000_PM_032", "A7-Pinhole": "211_SPM_000_PM_008",
    "A*-JfW": "119_SRT_SPM_000_PM_033", "A*-Nervewrecker": "117_SRT_SPM_000_PM_028", "A*-DDM": "115_SRT_TAM_004_PM_016",
    "B1-WtaD": "203_SPM_000_PM_011", "B1-Third Wheel": "302_SPM_000_PM_008", "B1-Over the Fence": "316_SPM_000_PM_014", "B1-RoD": "319_SPM_000_PM_008", "B1-SaaS": "205_SPM_000_PM_003", "B1-Star": "Cloud_2_01_SRT_SPM_000_PM_004",
    "B2-Tomb": "213_SPM_000_PM_010", "B2-Star": "Cloud_2_02_SRT_SPM_000_PM_039", "B2-MotM": "221_SPM_002_PM_001", "B2-Moonshot": "223_SPM_000_PM_009", "B2-Higher Ground": "120_SPM_000_PM_029",
    "B3-Blown Away": "300a_SPM_000_PM_007", "B3-Star": "Cloud_2_03_SRT_SPM_002_PM_013", "B3-Sunshot": "222_SPM_004_PM_001", "B3-Eagle's Nest": "401_SPM_004_PM_008", "B3-Woosh": "409_SPM_000_PM_024",
    "B4-Self Help": "322_SPM_000_PM_008", "B4-Double-Plate": "321_SPM_000_PM_005", "B4-TRA": "215_SPM_000_PM_013", "B4-TRA Star": "Cloud_2_04_SRT_SPM_000_PM_017", "B4-RPS": "407_SPM_000_PM_018", "B4-ABUH": "310_SPM_000_PM_024", "B4-WAtC": "414_SPM_000_PM_007", "B4-Sphinx Star": "Cloud_2_04_SRT_SPM_002_PM_002",
    "B5-SES": "314_SPM_000_PM_012", "B5-Plates": "238_SPM_000_PM_018", "B5-Two Jammers": "239_SPM_000_PM_018", "B5-Iron Curtain": "311_SPM_000_PM_041", "B5-Chambers": "315_TAM_002_PM_001", "B5-Obelisk Star": "Cloud_2_05_SRT_TAM_003_PM_003",
    "B6-Crisscross": "208_SPM_000_PM_014", "B6-JDaW": "206_SPM_000_PM_021", "B6-Egyptian Arcade": "113_SPM_000_PM_036",
    "B7-AFaF": "224_SRT_SPM_000_PM_071", "B7-WLJ": "118_SPM_000_PM_062", "B7-BSbS": "301_SPM_000_PM_010", "B7-BSbS Star": "224_SRT_SPM_000_PM_091", "B7-BLoM": "402_SPM_000_PM_020", "B7-Star": "Cloud_2_07_SRT_TAM_001_PM_004",
    "B*-Merry Go Round": "214_SRT_SPM_000_PM_025", "B*-Cat's Cradle": "306_SRT_SPM_000_PM_016", "B*-Peekaboo": "411_SRT_SPM_000_PM_014",
    "C1-Conservatory": "219_SPM_000_PM_008", "C1-MIA": "416_SPM_000_PM_026", "C1-Labyrinth": "114_SPM_000_PM_032", "C1-Blowback": "312_SPM_000_PM_032", "C1-Star": "Cloud_3_01_SRT_SPM_000_PM_017",
    "C2-ADaaF": "403_SPM_000_PM_015", "C2-Star": "Cloud_3_02_SRT_TAM_001", "C2-Rapunzel": "417_SPM_000_PM_029", "C2-Cemetery": "217_SPM_000_PM_040", "C2-Short Wall": "418_SPM_000_PM_014",
    "C3-Three Connectors": "225_SPM_000_PM_044", "C3-Jammer Quarantine": "317_SPM_000_PM_024", "C3-BSLS": "229_SPM_000_PM_070", "C3-Weathertop": "318_SPM_000_PM_026", "C3-Star": "Cloud_3_03_SRT_SPM_000_PM_069",
    "C4-Armory": "313_SPM_000_PM_016", "C4-Oubliette": "405_SRT_SPM_000_PM_050", "C4-Oubliette Star": "405_SRT_SPM_000_PM_047", "C4-Stables": "216_SPM_000_PM_015", "C4-Throne Room": "408_SPM_000_PM_033", "C4-Throne Room Star": "408_SRT_SPM_000_PM_034",
    "C5-Time Flies": "328_SPM_000_PM_016", "C5-Time Flies Star": "Cloud_3_05_SRT_SPM_003_PM_012", "C5-Time Crawls": "232_SPM_000_PM_012", "C5-Dumbwaiter": "309_SPM_000_PM_018", "C5-Dumbwaiter Star": "Cloud_3_05_SRT_SPM_002_PM_016", "C5-UCaJ": "404_SPM_000_PM_022", "C5-UCAJ Star": "Cloud_3_05_SRT_SPM_000_PM_035",
    "C6-Seven Doors": "234_SPM_000_PM_015", "C6-Star": "Cloud_3_06_SRT_SPM_000_PM_008", "C6-Circumlocution": "226_SPM_000_PM_039", "C6-Two Way Street": "112_SPM_000_PM_034",
    "C7-Carrier Pigeons": "230_SPM_000_PM_019", "C7-DMS": "308_SPM_000_PM_017", "C7-Star": "Cloud_3_07_SRT_SPM_000_PM_021", "C7-Prison Break": "227_SPM_002_PM_033", "C7-Crisscross": "233_SPM_000_PM_015",
    "C*-Unreachable Garden": "235_SRT_SPM_000_PM_037", "C*-Nexus": "320_SRT_SPM_000_PM_046", "C*-Cobweb": "504_SRT_SPM_000_PM_021",
    "CM-Star": "Islands_01_SRT_SPM_000_PM_003", "F0-Star": "Secret_28_SRT_SPM_000_PM_004", "F3-Star": "LeapOfFaith_PM_010"
}

a_markers = [
    "A1-PaSL", "A1-Beaten Path", "A1-Outnumbered", "A1-OtToU", "A1-ASooR", "A1-Trio", "A1-Peephole", "A1-Star",
    "A2-Guards", "A2-Hall of Windows", "A2-Suicide Mission", "A2-Star",
    "A3-Stashed for Later", "A3-ABTU", "A3-ABTU Star", "A3-Swallowed the Key", "A3-AEP", "A3-Clock Star",
    "A4-Branch it Out", "A4-Above All That", "A4-Push it Further", "A4-Star", "A4-DCtS",
    "A5-Two Boxes", "A5-Two Boxes Star", "A5-YKYMCTS", "A5-Over the Fence", "A5-OLB", "A5-FC", "A5-FC Star",
    "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic", "A6-Star",
    "A7-LFI", "A7-Trapped Inside", "A7-Two Buzzers", "A7-Star", "A7-WiaL", "A7-Pinhole"
]

def rand(min, max):
    global seed
    seed = (214013 * seed + 2531011) % 2147483648
    if min == max: return min
    return (seed % (max - (min - 1))) + min

markers = [
    [lambda: isWorldOpen("A1"), [
        "A1-Peephole", "A1-PaSL", "A1-Outnumbered", "A1-ASooR",
        "A1-OtToU", "A1-Trio", "A1-Beaten Path", "A1-Star"
    ]],
    [lambda: isWorldOpen("A2"), [
        "A2-Hall of Windows", "A2-Guards", "A2-Suicide Mission", "A2-Star"
    ], "A2"],
    [lambda: isWorldOpen("A3"), [
        "A3-ABTU Star", "A3-ABTU", "A3-AEP", "A3-Swallowed the Key",
        "A3-Stashed for Later", "A3-Clock Star"
    ], "A3"],
    [lambda: isWorldOpen("A4"), [
        "A4-Push it Further", "A4-Branch it Out", "A4-Above All That", "A4-Star"
    ], "A4"],
    [lambda: isWorldOpen("A4") and not locked["Connector"], [
        "A4-DCtS"
    ], "A4"],
    [lambda: isWorldOpen("A5"), [
        "A5-Two Boxes", "A5-Two Boxes Star", "A5-Over the Fence", "A5-YKYMCTS",
        "A5-OLB", "A5-FC", "A5-FC Star",
    ], "A5"],
    [lambda: isWorldOpen("A6"), [
        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic",
        "A6-Star"
    ], "A6"],
    [lambda: isWorldOpen("A7"), [
        "A7-Two Buzzers", "A7-Pinhole", "A7-LFI", "A7-WiaL",
        "A7-Trapped Inside", "A7-Star"
    ], "A7"],
    [lambda: isWorldOpen("A8"), [
        "A*-DDM", "A*-Nervewrecker", "A*-JfW"
    ], "A8"],
    [lambda: isWorldOpen("B1"), [
        "B1-SaaS", "B1-WtaD", "B1-Third Wheel", "B1-Over the Fence",
        "B1-RoD", "B1-Star"
    ], "B1"],
    [lambda: isWorldOpen("B2"), [
        "B2-Higher Ground", "B2-Tomb", "B2-MotM", "B2-Moonshot",
        "B2-Star"
    ], "B2"],
    [lambda: isWorldOpen("B3"), [
        "B3-Sunshot", "B3-Blown Away", "B3-Eagle's Nest", "B3-Woosh",
        "B3-Star"
    ], "B3"],
    [lambda: isWorldOpen("B4"), [
        "B4-TRA", "B4-ABUH", "B4-Double-Plate", "B4-Self Help",
        "B4-RPS", "B4-WAtC", "B4-TRA Star"
    ], "B4"],
    [lambda: isWorldOpen("B4") and not locked["Connector"], [
        "B4-Sphinx Star"
    ], "B4"],
    [lambda: isWorldOpen("B5"), [
        "B5-Plates", "B5-Two Jammers", "B5-Iron Curtain", "B5-SES",
        "B5-Chambers"
    ], "B5"],
    [lambda: isWorldOpen("B5") and ((not locked["Connector"] and not locked["Fan"]) or not locked["Cube"]), [
        "B5-Obelisk Star"
    ], "B5"],
    [lambda: isWorldOpen("B6"), [
        "B6-Egyptian Arcade", "B6-JDaW", "B6-Crisscross"
    ], "B6"],
    [lambda: isWorldOpen("B7"), [
        "B7-WLJ", "B7-AFaF", "B7-BSbS Star", "B7-BSbS",
        "B7-BLoM"
    ], "B7"],
    [lambda: isWorldOpen("B7") and not locked["Fan"], [
        "B7-Star"
    ], "B7"],
    [lambda: isWorldOpen("B8"), [
        "B*-Merry Go Round", "B*-Cat's Cradle", "B*-Peekaboo"
    ], "B8"],
    [lambda: isWorldOpen("C1"), [
        "C1-Labyrinth", "C1-Conservatory", "C1-Blowback", "C1-Star"
    ], "C1"],
    [lambda: isWorldOpen("C1") and not locked["Cube"], [
        "C1-MIA"
    ], "C1"],
    [lambda: isWorldOpen("C2"), [
        "C2-Cemetery", "C2-ADaaF", "C2-Rapunzel", "C2-Short Wall",
        "C2-Star"
    ], "C2"],
    [lambda: isWorldOpen("C3"), [
        "C3-Three Connectors", "C3-BSLS", "C3-Jammer Quarantine", "C3-Weathertop",
        "C3-Star"
    ], "C3"],
    [lambda: isWorldOpen("C4"), [
        "C4-Stables", "C4-Armory", "C4-Oubliette Star", "C4-Oubliette",
        "C4-Throne Room Star"
    ], "C4"],
    [lambda: isWorldOpen("C4") and not locked["Cube"], [
        "C4-Throne Room"
    ], "C4"],
    [lambda: isWorldOpen("C5"), [
        "C5-Time Crawls", "C5-Dumbwaiter", "C5-Time Flies", "C5-UCaJ",
        "C5-Time Flies Star"
    ], "C5"],
    [lambda: isWorldOpen("C5") and not locked["Cube"], [
        "C5-UCAJ Star", "C5-Dumbwaiter Star"
    ], "C5"],
    [lambda: isWorldOpen("C6"), [
        "C6-Two Way Street", "C6-Circumlocution", "C6-Seven Doors", "C6-Star"
    ], "C6"],
    [lambda: isWorldOpen("C7"), [
        "C7-Prison Break", "C7-Carrier Pigeons", "C7-Crisscross", "C7-DMS",
        "C7-Star"
    ], "C7"],
    [lambda: isWorldOpen("C8"), [
        "C*-Nexus"
    ], "C8"],
    [lambda: isWorldOpen("C8") and (not locked["Connector"] or not locked["Cube"]), [
        "C*-Cobweb", "C*-Unreachable Garden"
    ], "C8"],
    [lambda: isWorldOpen("CMessenger"), [
        "CM-Star"
    ], "CMessenger"],
    [lambda: not locked["F1"] and (not locked["Connector"] or not locked["F3"]), [
        "F0-Star"
    ]],
    [lambda: not locked["F3"], [
        "F3-Star"
    ]]
]

try:
    while True:
        talosProgress = {}
        PROB_BOTH_HUBS = 25

        seed = base_seed
        # print(seed)
        SetVarValue("Randomizer_Seed", seed)
        SetVarValue("PaintItemSeed", rand(0, 8909478))
        SetVarValue("Code_Floor4", rand(1, 999))
        SetVarValue("Code_Floor5", rand(1, 999))
        codeF6 = 0
        for i in range(1, 4):
            digit = rand(4, 9)
            SetVarValue("Code_Floor6" + str(i), digit)
            SetVar("Code_Floor6DigitSeen" + str(i))
            codeF6 = codeF6*10 + digit
        SetVarValue("Code_Floor6", codeF6)

        locked = {
            "A1 Gate": [],
            "A Gate": [],
            "B Gate": [],
            "C Gate": [],
            "A Star": ["**1", "**2", "**3", "**4", "**5", "**6", "**7", "**8", "**9", "**10"],
            "B Star": ["**11", "**12", "**13", "**14", "**15", "**16", "**17", "**18", "**19", "**20"],
            "C Star": ["**21", "**22", "**23", "**24", "**25", "**26", "**27", "**28", "**29", "**30"],
            "Connector": ["ML1", "MT1", "MT2"],
            "Cube": ["ML2", "MT3", "MT4", "MZ1"],
            "Fan": ["ML3", "MS1", "MT5", "MT6", "MZ2"],
            "Recorder": ["MJ1", "MS2", "MT7", "MT8", "MZ3"],
            "Platform": ["MI1", "ML4", "MO1", "MT9", "MT10", "MZ4"],
            "F1": ["NL1", "NL2", "NZ1", "NZ2"],
            "F2": ["NL3", "NL4", "NL5", "NL6", "NO1", "NT1", "NT2", "NT3", "NT4"],
            "F3": ["NI1", "NI2", "NI3", "NI4", "NJ1", "NJ2", "NL7", "NL8", "NS1", "NZ3"],
            "F4": ["NJ3", "NL9", "NO2", "NO3", "NS2", "NS3", "NT5", "NT6", "NT7", "NT8", "NZ4", "NZ5"],
            "F5": ["NI5", "NI6", "NJ4", "NL10", "NO4", "NO5", "NO6", "NO7", "NS4", "NT9", "NT10", "NT11", "NT12", "NZ6"],
            "F6": ["EL1", "EL2", "EL3", "EL4", "EO1", "ES1", "ES2", "ES3", "ES4"]
        }

        randomization_mode = "Default"
        portals = False
        loop = 0
        scavenger = 0
        SetVarValue("Randomizer_Mode", 1)
        SetVarValue("Randomizer_Scavenger", 0)
        SetVarValue("Randomizer_Loop", 0)
        SetVarValue("Randomizer_ShowAll", 1)

        portalOrder = [
            "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "ADevIsland",
            "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8",
            "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "CMessenger"
        ]
        portalIndexes = portalOrder
        startHub = "A"

        open_worlds = []
        WorldANames = []
        WorldBNames = []
        WorldCNames = []
        for index in range(len(portalOrder)):
            name = portalOrder[index]
            SetVarValue(name, index + 1)
            placedName = portalIndexes[index]
            if placedName[:1] == "A":
              WorldANames.append(name)
            elif placedName[:1] == "B":
              WorldBNames.append(name)
            elif placedName[:1] == "C":
              WorldCNames.append(name)

        markers = backup_markers

        checkGates = True
        allSpots = False
        placedItems = False
        placedStars = False
        placedFirstItems = False
        placedHub = False
        placedF1 = False
        availableMarkers = 0
        openMarkers = []

        closedMarkers = []
        WorldA = []
        WorldB = []
        WorldC = []
        for i in range(len(markers)):
            markerWorld = ""
            if len(markers[i]) > 2:
                markerWorld = markers[i][2]
            if markerWorld in WorldANames:
                WorldA.append(i)
            elif markerWorld in WorldBNames:
                WorldB.append(i)
            elif markerWorld in WorldCNames:
                WorldC.append(i)
            else:
                closedMarkers.append(i)

        arranger = ""
        accessableArrangers = []
        lastHubs = []
        if startHub == "A":
            accessableArrangers = ["A Gate"]
            locked["A Gate"] = ["DI1", "DJ3", "DL1", "DZ2"]
            WorldA = addWorldMarkers(WorldA)
            lastHubs = ["B Gate", "C Gate"]
        elif startHub == "B":
            accessableArrangers = ["B Gate"]
            locked["B Gate"] = ["DI2", "DL2", "DT1", "DT2", "DZ3"]
            WorldB = addWorldMarkers(WorldB)
            lastHubs = ["A Gate", "C Gate"]
        elif startHub == "C":
            accessableArrangers = ["C Gate"]
            locked["C Gate"] = ["DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"]
            WorldC = addWorldMarkers(WorldC)
            lastHubs = ["A Gate", "B Gate"]

        while not allSpots or len(accessableArrangers) > 0:
            toRemove = []
            for i in range(len(closedMarkers)):
                index = closedMarkers[i]
                if markers[index][0]():
                    openMarkers.append(index)
                    toRemove.append(i)
                    availableMarkers = availableMarkers + len(markers[index][1])
            for i in range(len(toRemove)):
                closedMarkers.pop(toRemove[i] - (i - 1) - 1)

            if not allSpots:
                if (WorldA or WorldB or WorldC) or (locked["A Star"] or locked["B Star"] or locked["C Star"]):
                    if (WorldA or WorldB or WorldC):
                        if arranger == "A Gate" and startHub == "A":
                            accessableArrangers.append("B Gate")
                            accessableArrangers.append("C Gate")
                        elif arranger == "B Gate" and startHub == "B":
                            accessableArrangers.append("A Gate")
                            accessableArrangers.append("C Gate")
                        elif arranger == "C Gate" and startHub == "C":
                            accessableArrangers.append("A Gate")
                            accessableArrangers.append("B Gate")
                    elif not placedStars:
                        placedStars = True
                        accessableArrangers.append("A Star")
                        accessableArrangers.append("B Star")
                        accessableArrangers.append("C Star")
                else:
                    if not placedItems:
                        placedItems = True
                        accessableArrangers.append("Connector")
                        accessableArrangers.append("Cube")
                        accessableArrangers.append("Fan")
                        accessableArrangers.append("Recorder")
                        accessableArrangers.append("Platform")
                        accessableArrangers.append("F1")
                        accessableArrangers.append("F3")
                    elif placedItems and len(closedMarkers) == 0:
                        allSpots = True
                        accessableArrangers.append("F4")
                        accessableArrangers.append("F5")
                        accessableArrangers.append("F6")
                        accessableArrangers.append("A1 Gate")
                        accessableArrangers.append("F2")

            index = rand(0, len(accessableArrangers)-1)
            arranger = accessableArrangers.pop(index)
            sigils = locked[arranger]
            locked[arranger] = None

            if checkGates:
                if arranger == lastHubs[0] or arranger == lastHubs[1]:
                    if rand(0, 99) < PROB_BOTH_HUBS:
                        if startHub == "A":
                            sigils = ["DI2", "DL2", "DT1", "DT2", "DZ3",
                                      "DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"]
                        elif startHub == "B":
                            sigils = ["DI1", "DJ3", "DL1", "DZ2",
                                      "DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"]
                        elif startHub == "C":
                            sigils = ["DI1", "DJ3", "DL1", "DZ2",
                                      "DI2", "DL2", "DT1", "DT2", "DZ3"]
                        for i in range(len(accessableArrangers)):
                            if accessableArrangers[i][2:] == "Gate":
                                accessableArrangers.pop(i)
                                break
                        locked["A1 Gate"] = ["DJ1", "DJ2", "DZ1"]
                        locked["A Gate"] = None
                        locked["B Gate"] = None
                        locked["C Gate"] = None
                        WorldA = addWorldMarkers(WorldA)
                        WorldB = addWorldMarkers(WorldB)
                        WorldC = addWorldMarkers(WorldC)
                    else:
                        uniqueSigils = []
                        if arranger == "A Gate":
                            if startHub == "B":
                                uniqueSigils = ["DT3"]
                                locked["C Gate"] = ["DJ4", "DJ5", "DL3", "DT4", "DZ4"]
                            elif startHub == "C":
                                uniqueSigils = ["DT1"]
                                locked["B Gate"] = ["DI2", "DL2", "DT2", "DZ3"]
                            locked["A1 Gate"] = ["DJ1", "DJ2", "DZ1"]
                            sigils = ["DI1", "DJ3", "DL1", "DZ2"]
                            WorldA = addWorldMarkers(WorldA)
                        elif arranger == "B Gate":
                            if startHub == "A":
                                uniqueSigils = ["DJ1", "DJ4", "DJ5"]
                                locked["A1 Gate"] = ["DJ2", "DZ1"]
                                locked["C Gate"] = ["DL3", "DT3", "DT4", "DZ4"]
                            elif startHub == "C":
                                uniqueSigils = ["DJ1", "DJ2", "DJ3"]
                                locked["A1 Gate"] = ["DZ1"]
                                locked["A Gate"] = ["DI1", "DL1", "DZ2"]
                            sigils = ["DI2", "DL2", "DT1", "DT2", "DZ3"]
                            WorldB = addWorldMarkers(WorldB)
                        elif arranger == "C Gate":
                            if startHub == "A":
                                uniqueSigils = ["DI2"]
                                locked["B Gate"] = ["DL2", "DT1", "DT2", "DZ3"]
                            elif startHub == "B":
                                uniqueSigils = ["DI1"]
                                locked["A Gate"] = ["DJ3", "DL1", "DZ2"]
                            locked["A1 Gate"] = ["DJ1", "DJ2", "DZ1"]
                            sigils = ["DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"]
                            WorldC = addWorldMarkers(WorldC)

                        tempOpenMarkers = []
                        tempAvailableMarkers = 0
                        for index in closedMarkers:
                            if markers[index][0]():
                                tempOpenMarkers.append(index)
                                tempAvailableMarkers = tempAvailableMarkers + len(markers[index][1])

                        for i in uniqueSigils:
                            index = rand(0, tempAvailableMarkers - 1)
                            for j in range(len(tempOpenMarkers)):
                                if index >= len(markers[tempOpenMarkers[j]][1]):
                                    index = index - len(markers[tempOpenMarkers[j]][1])
                                else:
                                    randMarker = markers[tempOpenMarkers[j]][1].pop(index)
                                    SetCodeValue(translate[randMarker], i)
                                    tempAvailableMarkers = tempAvailableMarkers - 1
                                    break
                    checkGates = False
            elif not checkGates:
                if arranger == "A Gate":
                    WorldA = addWorldMarkers(WorldA)
                elif arranger == "B Gate":
                    WorldB = addWorldMarkers(WorldB)
                elif arranger == "C Gate":
                    WorldC = addWorldMarkers(WorldC)
            placeSigils(sigils)

        SetVar("Randomizer_Generated")


        a_sigils = ""
        for marker in a_markers:
            a_sigils += GetVarValue(translate[marker]) + ";"
        grey_count = len(re.findall(r"E[IJLOSTZ]\d+;", a_sigils))
        l_count = len(re.findall(r"NL\d+;", a_sigils))
        z_count = len(re.findall(r"NZ\d+;", a_sigils))
        if grey_count >= 9 and l_count >= 2 and z_count >= 2:
            output = "{}, {}, {}".format(base_seed, checksum(), GetVarValue("Code_Floor6"))
            print(output)
            file = open("output.txt", "a")
            file.write(output + "\n")
            file.close()
        base_seed += 1
        if base_seed >= max_seed:
            print("Out of seeds")
            break
except KeyboardInterrupt:
    print("Stopped while working on", base_seed)