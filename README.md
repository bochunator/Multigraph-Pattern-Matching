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
