Name: Vishlen V. Pillay
Algorithmic Trading Developer
Technical Assignment Part I
************************************

Efficiency Mechanisms:

Object-Oriented Programming(OOP) was used for the following efficiency reasons: 
	- OOP is faster and easier to execute
	- OOP provides a clear structure for the program
	- OOP helps to make the code easier to maintain, modify and debug
	- OOP makes it possible to create full reusable applications with less code and shorter development time
The assessment question provides details for the order object.  

Threading: threading was used to handle concurrent tasks at the same time.  
			- it reduces complexity of big applications
			- improves performance of the application drastically.
			- implementation of thread safe manipulation of treemap was implemented to alleviate errors. 
This will keep the order book running smoothly without tasks waiting for each other to complete before starting.

Single Responsibility Principle was used to avoid creating long methods to accomplish multiple tasks, long methods can lead to performance bottlenecks.
Each method has a single responsibility and focus on a specific functionality.  

A linkedList datastructure was chosen to be used instead of an arraylist as manipulation is faster with a linkedList.  Manipulation of a linked list requires no bit shifting in memory.
A TreeMap datastructure was chosen over a hashMap, because the TreeMap will keep all the data sorted by Key in a natural sorting manner.  sorting of the key is necessary to complete this task. 
In Java when passing a TreeMap object as an argument, the TreeMap is passed by reference, I used this as an opportunity to pass in both BID and ASK TreeMaps as arguments when their details were 
needed in any functionality, esp when updating the maps.  
Much of the code is used to search the TreeMaps for orders, that involves cycling through each element in both TreeMaps(BID and ASK), I made use of flags within the code to reduce the amount of looping 
to be done.  For instance when deleting an order, we should use just the orderId to retrieve the order and delete it from the TreeMap.  I will first search the BID TreeMap for the ID and delete the order 
if it is found, I then set the deleted flag so that there is no need to search the ASK TreeMap if a delete has occured.  This reduces the complexity of the solution.  

**********************************************************************************************************************************************************************************************************

Data Structures: 

I used LinkedList to hold the list of orders per price level.
LinkedList is another common data structure in Java. 
It’s a doubly-linked list that stores elements in nodes. 
Each node contains a pointer to the next node and the previous node, making it efficient for adding and removing elements in the middle of the list. 
This came in handy to remove an order when it was updated, and moved to the end of the linkedlist, as specified functionality from the specs of the assignment. 
ArrayLists datastructure are very similiar to linkedlist. 
However, manipulation of arraylists is slow because internally it uses an array.  If an element is removed from an array all other elements are shifted.
With a LinkedList manipulation is faster because it uses a doubly linked list so no bit shifting is required in memory.


TreeMap data structure was chosen because I needed a way to store a Key-Value pair of data.
The price of the order is the key, and the LinkedList of orders is the value.   
I also needed this Key-Value pair to be sorted quickly and efficiently.  This sorting is important in order books and needs to happen on every insert to be efficient for an order book. 
Thus TreeMap was the best data structure to accomplish this task.  
The TreeMap data structure stores Key-Value pairs in a sorted order based on the natural ordering of its keys.
Main characteristics of TreeMaps: 
	- Sorted Order: Keys are always maintained in a sorted order.
	- No Null Keys: TreeMap does not allow null keys.
	- Non-synchronized: TreeMap is not thread-safe by default.  Thread-safe measures have been implemented in this solution. 
	- Performance: Operations like get(), put(), remove(), and containsKey() have a time complexity of O(log n).
HashMap data structure is also available to store Key-Value pair data, but would need manual sorting to be done to it.  
Searching for elements depending on the number of elements can bring down the performance of the Hashmap from an O(1) to an O(n) where n is the number of elements.  
TreeMap provides a performance of O(log(n)) for most operations like add(), remove() and contains().
A TreeMap can save memory (compared to HashMap) because it only uses the amount of memory needed to hold its item, unlike HashMap which uses contiguous region of memory, that can lead to defragmentation. 

*************************************************************************************************************************************************************************************************************

Solution approach: 

The assignment story explains that "The Orderbook should keep all distinct orders ordered on their price level."  
Immediately I thought of a Key-Value pair where the Key is the price level and the value is the order.  
As we go further through the assignment story we find out that multiple orders can have the same price and we should 
keep track of the distinct list of prices.  This means for each key(price) we need an array or list to store all the orders 
with the same price level.  So the Value in the Key-Value pair will need to be a list or array type of datastructure. 
After researching the different data structures I came to the conclusion that the best data structure to hold the list of 
orders will be a LinkedList.  
The LinkedList provides all the functionality necessary for this assignment and most efficiently.  It can be modified quick and easily 
without disturbing the memory bits. 
The assessment story provides us with details of the order object to be used.  
The assessment also mentions keeping the list in priority order.  Using a LinkedList solves the issue of priority.  
LinkedList allows us to quickly add or remove objects from anywhere in the list.  
To solve the priority problem, of adding modified orders to the lowest priority, I thought I would remove the object from the list, change the 
quantity of the object and then re-add the object to the list at the bottom, thus the order will come in with the lowest priority for 
that price.  Keep adding orders to the list from the bottom (lowest priority), all new orders will also have the lowest priority.  

An order book has 2 sides, a BID and an ASK side.  I used 2 TreeMap objects to represent each of these sides.  In an order book the 
BID and ASK sides of the book are updated independently of each other, only the traders watching the values of each side, use the information
to create, destroy or modify orders.  Thus the BID and ASK are udated with orders that are fed to them, likewise within my order book
the BID and ASK TreeMaps are updated independantly of each other.

Unique Id: 
I immediately thought of using a GUID/ UUID for this issue.  
UUID - Universally Unique Identifier or GUID - Globally Unique Identifier is a 128-bit long number in hex characters separated by '-'.
Java has simple generation methods to generate UUID's.  
Then I saw that efficiency and performance will be evaluated, I started thinking of maybe using an int for the Unique Id of the orders.  
I was going to implement the atomic integer method, which will increment on each order created.  But this could become complicated with 
different threads.  Thus with threading involved the best option was to generate the UUID unique Id.  
This will also allow for an elarge number of orders, whereas with an integer or long datatype the amount of unique Ids can be limited. 

Order.java
I created Order.java to hold the order details. 
This is the Order object with attributes:
- Id: String UUID, unique Identifier
- Price: double, price level of the order
- Quantity: integer representing the quantity of the order
- Side: boolean representing whether the order is buy(true) or sell(false)

Main.java
The Main.java is used to start the application and initialise the threading. It also holds the functionality of initialising the menu 
which when activated begins all functionality designed for the order book.

OrderGenerator.java
The OrderGenerator.java class is used to generate random orders using Random.  It is designed to generate quantity between 1 and 100.  
The price will be a double between 0 and 100. 
I created GenerateRandomOrder, where all values (price, quantity and side) is generated at random.
GenerateSamePriceOrder will generate orders for a specific side and price.
GenerateSameSideOrder will generate orders for a specific side.  
These are used to generate test order data for the purpose of the simulation.

Operations.java class 
I created Operations.java to hold all the functionality that will be used for the order book, such as adding orders to the book, 
deleting orders from the book, modifying existing orders.  This keeps all functionality in a neat clean class, easy to find and debug if necessary.  
In Java when passing the TreeMap as an argument, the TreeMap is passed by reference.  
- AddOrder: It was necessary to write lock the thread for addOrder until the addition of the new order was completed.  This prevents concurrent update errors from different threads to the same TreeMap. 
- 

I like my applications to run, so I created a menu and simulation of the functionality of the order book, so that one may experience the 
functionality first hand.  
Together with this menu, I needed a way for the user to view the orders that were created, deleted and modified.  I created a method to 
print out the orders in tabular format to the console for the user to make use of.  

**********************************************************************************************************************************************************************************************************

