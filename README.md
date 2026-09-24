# Multigraph Pattern Matching

Master's thesis project: implementation and experimental comparison of graph pattern matching algorithms for multigraphs in Java.

## Overview

This project investigates subgraph isomorphism in multigraphs, where multiple parallel edges may exist between the same pair of vertices.

The project implements and compares four pattern-matching algorithms:

* Brute Force
* Pruned Backtracking
* Ullmann
* VF2

The main focus of the experimental comparison is the Ullmann and VF2 algorithms, adapted to support multigraph-specific properties such as parallel edges and self-loops.

## Using the algorithms

### 1. Generate a test case

To generate a graph containing an embedded pattern, run:

```text
generate <graphVertices> <graphEdges> <patternVertices> <patternEdges>
```

For example:

```text
generate 30 200 12 30
```

This creates an experiment containing a target graph and a pattern graph.

### 2. Run a matching algorithm

After generating an experiment, run:

```text
test <algorithm> <experimentId>
```

For example:

```text
test VF2 1
test Ullmann 1
```

Available algorithms:

```text
BruteForce
PrunedBacktracking
Ullmann
VF2
```

The program reports the execution time, number of matches, visited states and rejected candidates.

### 3. Use your own graph and pattern

The application stores graph instances as Graphviz DOT files.

An experiment consists of:

```text
pattern.dot
graph.dot
```

where `pattern.dot` contains the graph to search for and `graph.dot` contains the target graph.

To use your own data, place the two DOT files in an experiment directory following the project's experiment storage structure.

The target graph should contain the pattern if you want to test a case with at least one expected match.

The implementation supports multigraph-specific properties such as:

* parallel edges
* self-loops
* edge multiplicity

### Running from an IDE

If you run the project from IntelliJ IDEA, use:

```text
pl.bochunator.app.Main
```

as the application entry point.

There is no need to modify the matching algorithms themselves. The algorithms can be selected through the command-line interface.

### 4. Using the matchers in your own application

The command-line application included in this repository is primarily provided for running experiments and comparing the implemented algorithms.

The matching algorithms themselves are implemented independently in the `matcher` package. They can be used from another Java application by creating a graph and a pattern using JGraphT and passing them to the selected matcher.

For example, the repository contains implementations of:

```text
BruteForce
PrunedBacktracking
Ullmann
VF2
```

A custom application can create its own entry point and use the matcher directly instead of using the experiment command-line interface.

The existing `Main` class is only the entry point for the example/experimental application. When integrating the matchers into another application, it can be replaced with the application's own entry point.

The general usage is the same for all four matchers:

```java
Graph<Integer, DefaultEdge> graph = ...;
Graph<Integer, DefaultEdge> pattern = ...;

Matcher matcher = new VF2();

List<Map<Integer, Integer>> matches = matcher.findMatches(pattern, graph);
```

The matcher can be replaced with any of the implemented algorithms:

```java
Matcher bruteForce = new BruteForce();
Matcher prunedBacktracking = new PrunedBacktracking();
Matcher ullmann = new Ullmann();
Matcher vf2 = new VF2();
```

Each matcher can then be used with the same target graph and pattern:

```java
List<Map<Integer, Integer>> matches = matcher.findMatches(pattern, graph);
```

The returned mappings represent the matches of the pattern in the target graph.

The command-line application and experiment framework are separate from the matching implementations. The `Main` class is used as the entry point for the included experimental application. When integrating the matchers into another Java application, the application can use its own entry point and directly instantiate the selected matcher.

This makes it possible to use the implemented algorithms independently of the graph generation, benchmarking and result storage components.



## Approach

The implementation was developed incrementally.

First, the algorithms were implemented and tested for simple graphs. Brute Force was used as a reference implementation for verifying the correctness of more advanced approaches. Pruned Backtracking was then introduced to reduce unnecessary search.

After validating the basic implementations, the algorithms were adapted to multigraphs. The adaptations account for edge multiplicity and self-loops when checking whether a partial or complete mapping is valid.

The final implementations were evaluated using the same generated input instances to make the comparison reproducible.

## Experimental evaluation

The project includes an experiment framework for generating graph instances, running the algorithms, collecting measurements and storing the results.

The following metrics were recorded:

* execution time
* number of visited search states
* number of rejected candidates

The experiments were performed on identical input graphs for the compared algorithms.

The collected results were subsequently analyzed quantitatively, qualitatively and statistically.

## Results

For the tested multigraph instances, the Ullmann implementation achieved lower execution times and visited fewer search states than the implemented VF2 algorithm.

The experiments also showed a difference in the number of rejected candidates. This metric requires more careful interpretation because the two algorithms eliminate candidates at different stages of the search process.

The results apply to the tested input generation method, graph sizes and implementations. They should not be interpreted as a general statement that Ullmann is always faster than VF2.

## Multigraph support

The implementation uses JGraphT as a standardized graph representation and API, while the pattern-matching algorithms themselves are implemented in this project.

The algorithms explicitly account for multigraph properties, including:

* multiple parallel edges
* self-loops
* edge multiplicity during candidate validation

For example, candidate mappings are not considered valid merely because an edge exists between two vertices. The implementation also verifies that the target graph contains the required number of parallel edges.

## Technologies

* Java 25
* JGraphT
* Maven
* Graph algorithms
* Multigraphs
* Experimental data analysis
