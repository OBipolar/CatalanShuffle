# Catalan Shuffle
* A Discrete Mathematics Senior Project
* Under supervision of [Dr.Tetali Prasad](https://people.math.gatech.edu/~tetali/)
* Forked from [Victor Chen](https://github.com/yiqic1993)

### Introduction 

Catalan structures are defined as combinatorial interpretation of [Catalan Numbers](http://mathworld.wolfram.com/CatalanNumber.html).  Each type of Catalan structure can be counted using Catalan Number, and satisfies the basic properties of Catalan Numbers. Below is lattice path, one of the most well known Catalan structure. 

![Lattice Path - One most well known catalan structure](https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/Catalan_number_4x4_grid_example.svg/450px-Catalan_number_4x4_grid_example.svg.png)

The goal of this project is to perform simulation and visualization on the transition process over various Catalan structures.

### Supported Catalan Structures

In the book [Catlan Numbers](http://www.cambridge.org/us/academic/subjects/mathematics/discrete-mathematics-information-theory-and-coding/catalan-numbers?format=PB&isbn=9781107427747) by Dr.Richard P. Stanley, the author illustrates 214 different Catalan structures and below are structures I implemented in the code. 

* Single Dyck Path 
* Coupling Dyck Paths
* Multiple Dyck Paths (Same starting point)
* Parallel Dyck Paths (Parallel starting point)
* Polygon Triangulation  

### Run and Modify the code

Clone and import the project to IntelliJ Idea, main controller should be the start point of the program. (A corresponding jar file will be uploaded later).

The project follows a simple **MVC**(Model-View-Controller) structure, and modifications should be made to the relevant part. 