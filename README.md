# Project Overview

This repository contains implementations of 8 classes, each designed to solve a specific computational problem efficiently using advanced algorithms and data structures.

## Classes and Functionalities

### 1. **ImageSegmentation**

The `ImageSegmentation` class analyzes an image to determine:
- The number of distinct segments.
- The largest segment's size and color.

#### Key Features:
- Utilizes the **Union-Find algorithm** with path compression and union by size for efficiency.
- Tracks connected pixels to group them into segments.
- Outputs total distinct segments and details of the largest segment.

#### Optimization:
Handles large images efficiently with nearly constant-time Union-Find operations.

---

### 2. **RPG**

The `RPG` class calculates the maximum possible damage a hero can inflict on a dragon over a set number of rounds.

#### Key Features:
- Uses **memoization** to avoid redundant calculations.
- Evaluates the best strategy: attack directly or boost for future rounds.
- Computes optimal damage over a given time horizon.

---

### 3. **Mafia**

The `Mafia` class determines attack bounds for mafia members based on their level and range, adhering to specific rules.

#### Key Features:
- Employs **stack-based algorithms** to calculate bounds efficiently.
- Combines level-based and range-based constraints.
- Outputs attack bounds for each member as pairs of indices.

#### Optimization:
Runs in **O(N)** time using stacks to calculate level-based bounds.

---

### 4. **ObservationStations**

The `ObservationStationAnalysis` class analyzes and optimizes observation station placements.

#### Key Features:
- Computes the **convex hull** of station coordinates using the Graham Scan algorithm.
- Finds the two stations farthest apart.
- Calculates the coverage area of the convex hull.
- Assesses the impact of adding a new station.

#### Optimization:
Efficiently sorts points by polar angle to minimize computation.

---

### 5. **Exam**

The `Exam` class identifies students who rank in the top 20% across all subjects for admission.

#### Key Features:
- Uses a **PriorityQueue** to rank students efficiently.
- Handles ties at cutoff scores fairly.
- Sorts eligible students by total score and ID.

#### Final Output:
IDs and scores of students who qualify.

---

### 6. **RoadStatus**

The `RoadStatus` class manages traffic lights at a T-junction to regulate traffic flow.

#### Key Features:
- Dynamically adjusts green lights based on the number of waiting cars.
- Follows priority rules, favoring roads with more cars.
- Handles real-time events with a system clock.

#### Final Output:
Efficient and fair traffic flow management.

---

### 7. **IntervalST**

The `IntervalST` class manages a collection of numerical intervals for operations like insertion, deletion, and overlap searching.

#### Key Features:
- Implements an **Interval Search Tree** to manage intervals dynamically.
- Supports:
  - Insertion and deletion of intervals.
  - Efficient overlap searches.
- Dynamically adjusts tree properties for balanced operations.

#### Final Output:
Efficient interval management with optimized performance.

---

### 8. **ImageMerge**

The `ImageMerge` class merges overlapping bounding boxes based on their Intersection over Union (IoU) threshold.

#### Key Features:
- Uses **union-find** to group overlapping boxes.
- Employs an **interval tree** for efficient overlap detection.
- Outputs merged boxes sorted by specified criteria.

#### Optimization:
Combines union-find and interval trees to handle large datasets efficiently.




