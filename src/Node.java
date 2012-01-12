/**
* A generic Node for a doubly linked list.
* @author Xunxen Xyto
*/
class Node<T>{

	private Node<T> next,prev;
	private T data;
	/**
	* Initiallizes a node in a doubly linked list with specified
	* next and previous nodes and data.
	* @param p
	*  Previous node in the linked list.
	* @param d
	*  Data for the current node.
	* @param n
	*  Next node in the linked list.
	* @postcondition
	*  Node is created with given data and surrounding nodes.
	*/
	public Node(Node<T> p, T d, Node<T> n){
	
		prev=p;
		next=n;
		data=d;
	
	}
	
	/**
	* Get the data stored in this node.
	* @param - none
	* @return current node's data.
	*/
	public T getData(){
	
		return data;
	
	}
		
	/**
	* Set the current node's data value.
	* @param d
	*  Data value to set this node's value to.
	* @postcondition <code>data==d</code>
	*/
	public void setData(T d){
	
		data=d;
	
	}
	
	/**
	* Set the pointer to the next element.
	* @param n
	*  Node to set the next element to.
	* @postcondition
	*  next is set to a new node value.
	*/
	public void setNext(Node<T> n){
	
		next=n;
	
	}
	
	/**
	* Set the pointer to the previous element.
	* @param p
	*  Node to set the previous element to.
	* @postcondition
	*  prev is set to a new node value.
	*/
	public void setPrev(Node<T> n){
	
		prev=n;
	
	}
	
	/**
	* Get the pointer to the previous node.
	* @param - none
	* @return Pointer to the previous node.
	*/
	public Node<T> getPrev(){
	
		return prev;
		
	}
	
	/**
	* Get the pointer to the next node.
	* @param - none
	* @return Pointer to the next node.
	*/
	public Node<T> getNext(){
	
		return next;
		
	}

}