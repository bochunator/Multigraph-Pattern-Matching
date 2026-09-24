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

## Quick Start

The project can be run directly from the command line using Maven.

Compile the project:

```bash
mvn compile
```

Generate a test case:

```bash
mvn exec:java "-Dexec.args=generate 7 15 4 8"
```

Run the implemented algorithms on the generated experiment:

```bash
mvn exec:java "-Dexec.args=test BruteForce 1"
mvn exec:java "-Dexec.args=test PrunedBacktracking 1"
mvn exec:java "-Dexec.args=test Ullmann 1"
mvn exec:java "-Dexec.args=test VF2 1"
```

Each run reports the execution time, number of matches, visited search states and rejected candidates.

A short demonstration of the complete workflow is shown below:

[Watch the demo](demo.mp4)

The following sections explain how to use custom graph data and integrate the matchers into another Java application.

## Using the algorithms

### Using your own graph and pattern

The application stores graph instances as Graphviz DOT files.

An experiment consists of:

```
pattern.dot
graph.dot
```

where pattern.dot contains the graph to search for and graph.dot contains the target graph.

To use your own data, place the two DOT files in an experiment directory following the project's experiment storage structure.

The target graph should contain the pattern if you want to test a case with at least one expected match.

The implementation supports multigraph-specific properties such as:

* parallel edges
* self-loops
* edge multiplicity
* 
### Running from an IDE

The command-line interface can then be used to generate experiments and run the implemented algorithms.

### Using the matchers in your own application

The matching algorithms are implemented independently in the matcher package and can be used directly from another Java application.

The repository contains implementations of:

```
BruteForce
PrunedBacktracking
Ullmann
VF2
```

Create a graph and a pattern using JGraphT and instantiate the desired matcher:

```
Graph<Integer, DefaultEdge> graph = ...;
Graph<Integer, DefaultEdge> pattern = ...;

Matcher matcher = new VF2();

List<Map<Integer, Integer>> matches = matcher.findMatches(pattern, graph);
```

The same target graph and pattern can be used with any of the four implementations:

```
Matcher bruteForce = new BruteForce();
Matcher prunedBacktracking = new PrunedBacktracking();
Matcher ullmann = new Ullmann();
Matcher vf2 = new VF2();
```

The returned mappings represent the matches of the pattern in the target graph.

The command-line application and experiment framework are provided for graph generation, benchmarking and comparison. An application integrating the matchers directly can use its own entry point.