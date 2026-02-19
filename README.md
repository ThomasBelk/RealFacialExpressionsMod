## Mirror your facial expressions to your character in real time!

Real Facial Animations uses a companion app to track your facial expressions through your webcam and transmit them to the game in real time.

**Note: The plugin only transmits facial blendshape data. No video or image data ever leaves your PC.**

## Disclaimers
- Real Facial Expressions is in early access and, therefore, is not currently recommended for large servers.
- Real Facial Expressions uses a companion app to send data to the server. The connection fields are intended to remain private. Try not to leak them, or you will at least have to reset your faceId with **/rfe resetFaceId**
- There will very likely be **conflicts** with other player animation plugins, particularly those that modify **Server/Models/Human/Player.json**.

## Lisence
The project is currently licensed as All Rights Reserved. I plan to revisit this after further researching more licensing options.
## Installation Guide
### User
1) Install the companion app.
2) Connect to the server in Hytale.
3) Run **/rfe faceId** in-game and copy the generated ID into the companion app.
4) Enter the server IP and the port into the companion app. The default port is **25590**.
    - Note: your faceId should not change so you will only have to do this once.
5) Once you tab back into the game, your expressions should be mirrored on your character.
### Server Owners
1) Install plugin.
2) Open the configured UDP port to your players.
## Current Roadmap (Subject to Change)
- Add profiles for multiple servers to the companion app, along with some general UI improvements.
- Improve validation of incoming packets to better handle spam and oversized payloads.
- Create video tutorials for setup and animation tuning.
- Allow players to configure custom facial tracking thresholds (e.g., how large a smile must be to trigger an animation).
## Long Term Goals and Current Blockers
- **More animations**. More animations. The primary blocker is the current state of Hytale’s animation system. From my understanding, Hytale does not support playing multiple animations within the same animation slot simultaneously. To work around this, the 9 look-direction animations, 5 eyebrow animations, 6 eyelid animations (3 per eye), and 5 mouth animations are combined via a script into unique animations representing every possible state. This results in 2,025 generated animations. Since each additional animation dramatically increases this count, no further animations are planned until the animation system receives an overhaul.
- Offer players the ability to define custom animation triggers. The goal is to allow playful mappings, such as triggering a horn wiggle animation when the player blinks.