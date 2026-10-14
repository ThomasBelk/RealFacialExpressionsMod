import os
import json
import itertools

# run in project root
BASE_PATH = "Characters/Animations/RealFacialAnimations"
ROOT = "src/main/resources/Common/Characters/Animations/RealFacialAnimations"
PATCH_FILE = "src/main/resources/Server/Models/Human/Player.patch"
GENERATED_DIR = "src/main/resources/Common/Characters/Animations/RealFacialAnimations/Generated"

FOLDER_ORDER = [
    "Eyes",
    "LeftEyelid",
    "RightEyelid",
    "Brows",
    "Mouth"
]

def strip_ext(name):
    return os.path.splitext(name)[0]

def main():
    try:
        print("Generating Animations Json")
        os.makedirs(GENERATED_DIR, exist_ok=True)

        animations_by_folder = []

        for folder in FOLDER_ORDER:
            folder_path = os.path.join(ROOT, folder)

            if not os.path.isdir(folder_path):
                print(f"Missing folder: {folder_path}")
                continue

            files = [
                f for f in os.listdir(folder_path)
                if os.path.isfile(os.path.join(folder_path, f))
            ]

            if not files:
                print(f"No animations found in {folder}")
                continue

            animations_by_folder.append([
                {
                    "folder": folder,
                    "file": f
                }
                for f in sorted(files)
            ])

        result = {}

        for combo in itertools.product(*animations_by_folder):
            key = "_".join(strip_ext(a["file"]) for a in combo)
            out_anim_path = os.path.join(GENERATED_DIR, f"{key}.blockyanim")

            combined_node_animations = {}

            # load base animation (first in combo)
            base = combo[0]
            base_path = os.path.join(ROOT, base["folder"], base["file"])

            with open(base_path, "r", encoding="utf-8") as f:
                combined_anim = json.load(f)

            # remove base nodeAnimations
            combined_anim["nodeAnimations"] = {}

            # merge nodeAnimations from all animations in combo
            for a in combo:
                src_path = os.path.join(ROOT, a["folder"], a["file"])

                with open(src_path, "r", encoding="utf-8") as f:
                    data = json.load(f)

                node_anims = data.get("nodeAnimations", {})

                for node, anim in node_anims.items():
                    combined_anim["nodeAnimations"][node] = anim

            # write combined .blockyanim
            with open(out_anim_path, "w", encoding="utf-8") as f:
                json.dump(combined_anim, f, indent=2)

            # associate anim names with path to anim file
            result[key] = {
                "Animations": [
                    {
                        "Animation": f"{BASE_PATH}/Generated/{key}.blockyanim"
                    }
                ]
            }

        wrappedResult = {
            "AnimationSets": result
        }

        # write .patch file
        with open(PATCH_FILE, "w", encoding="utf-8") as f:
            json.dump(wrappedResult, f, indent=2)

        print(f"Generated {len(result)} combinations and generated patch file: {PATCH_FILE}")

    except Exception as e:
        print("Error:", e)
        raise

if __name__ == "__main__":
    main()