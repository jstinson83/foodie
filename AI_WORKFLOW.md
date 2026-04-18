# The "Hobby Vibe" (Speed & Flow)

**Objective:** High-momentum development when it’s just for fun and not your day job.

## Step 1: Discovery
**Human Input:** A core idea, a rough list of features, and an estimated time budget.
**Initial Mindset:** I have a high-level goal but need to validate the technical feasibility and scope.
**Objective:** Define the core components and establish a 1-hour "sprint" roadmap.

**Prompts:**
* "I want to build [Project]. Break this down into the 4-5 high-level moving pieces and suggest an execution order for a 1-hour session."
* "Identify the moving parts here—what are the 3 biggest technical hurdles I'll hit first?"
* "What is the most 'minimalist' tech stack to get a 'Hello World' running tonight without unnecessary bloat?"

## Step 2: The Contract
**Human Input:** The high-level component list from Step 1 and your gut feeling on how you want the code to "feel" (e.g., "Keep it simple/modular").
**Initial Mindset:** I see the big pieces. Now I need to zoom in on the specifics so the AI doesn't hallucinate a weird implementation.
**Objective:** Clear documentation of the logic and "seams" for the specific chunk I'm about to build.

**Prompts:**
* **The Logic Map:** "Zoom into [Component]. Walk me through the specific functions and 3P libraries you’ll use. What is the internal logic flow before we start coding?"
* **The Integration Check:** "How exactly does this component talk to [Component B]? Show me the data payload 'contract' so the seams are clean."
* **The 'Laziness' Audit:** "Is there a simpler way to build this specific part? I want to avoid over-engineering—what’s the most direct path to a working version?”

## Step 3: The Blindspot
**Human Input:** A critical eye on the AI's suggestions and a willingness to pivot if the approach looks "heavy."
**Initial Mindset:** I’m moving quickly. I need a peer review to catch modern best practices or structural flaws I've missed.
**Objective:** Sanity check the design against technical debt and "unhappy paths."

**Prompts:**
* "Critique this approach: What is the most likely way this simple setup becomes a technical debt nightmare in three weeks?"
* "Is there a more 'modern' or lightweight library than [Library X] that fits a greenfield project better?"
* "Identify the 'unhappy path' here. If the input is [Edge Case], how does this design handle it?"

## Step 4: Execution
**Human Input:** Active code review and manual "vibe testing" of the output.
**Initial Mindset:** Time to generate. I want surgical, dense code that strictly follows the Step 2 contract.
**Objective:** Iterative delivery of "unsurprising" code.

**Prompts:**
* "Implement Chunk 1. Include inline assertions/logs to verify the 'Contract' from Step 2. Keep the code dense and surgical."
* "Build this chunk now. Once the logic is verified, I’ll have you generate the formal test suite."
* "Does this chunk require any new dependencies or specific 'hidden' state (env vars) that I need to configure manually?"
