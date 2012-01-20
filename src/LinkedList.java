/**
* A generic linked list management class
* @author Xunxen Xyto
*/
class LinkedList<T>{

    //head should always point to the element with prev==null.
    //curr should point to the last element added if the list has not been traversed.
    //tail should always point to the element with next==null.
    private Node<T> head,curr,tail;
    
    /**
    * Initializes an empty generic linked list.
    * @postcondition
    *  A linked list is created with a null head.
    */
    public LinkedList(){
    
        head=null;
        curr=head;
        tail=head;
    
    }
    
    /**
    * Append a new element to the end of the linked list.
    * @param data
    *  Data to add to the new node.
    * @postcondition
    *  Linked list has a new node at the end and remains a doubly linked list.
    */
    public void append(T data){
    
        if(head==null){
        
            head=new Node<T>(null, data, null);
            tail=head;
            curr=head;
            
        }else{
        
            Node<T> temp=new Node<T>(tail,data,null);
            tail.setNext(temp);
            tail=tail.getNext();
            curr=tail;
            
        }
    
    }
    
    /**
    * Prepend a new element to the beginning of the linked list.
    * @param data
    *  Data to add to the new node.
    * @postcondition
    *  Linked list has a new node at the beginning and remains a doubly linked list.
    */
    public void prepend(T data){
    
        if(head==null){
        
            head=new Node<T>(null, data, null);
            tail=head;
            curr=head;
            
        }else{
        
            first();
            Node<T> temp=new Node<T>(null,data,head);
            head.setPrev(temp);
            head=head.getPrev();
            curr=head;
            
        }
    
    }
    
    /**
    * Insert node after the current node
    * @param data
    *  Data to be inserted
    * @precondition
    *  There is at least one node
    * @postcondition
    *  There is a new node containing <code>data</code> after the current node. If
    *  the new node is at the end of the linked list, tail is set appropriately.
    */
    public void insertAfter(T data){
    
        if(head==null) return;
        if(curr==tail) append(data);
        else{
        
            Node<T> temp=new Node<T>(curr,data,curr.getNext());
            curr.setNext(temp);
            temp.getNext().setPrev(temp);
        
        }
    
    }
    
    /**
    * Insert node before the current node
    * @param data
    *  Data to be inserted
    * @precondition
    *  There is at least one node
    * @postcondition
    *  There is a new node containing <code>data</code> before the current node. If
    *  the new node is at the beginning of the linked list, head is set appropriately.
    */
    public void insertBefore(T data){
    
        if(head==null) return;
        if(curr==head) prepend(data);
        else{
        
            Node<T> temp=new Node<T>(curr.getPrev(),data,curr);
            curr.setPrev(temp);
            temp.getPrev().setNext(temp);
            
        }
    
    }
    
    /**
    * Remove the current node.
    * @precondition
    *  There is at least one node.
    * @postcondition Current node is removed, head is set to the new first node if
    *  the cursor was at the head, tail is set to the new first node if the cursor
    *  was at tail.
    *  the first element in the linked list.
    */
    public void remove(){
    
        if(curr==null) return;
    
        if(curr.getPrev()!=null&&curr.getNext()!=null){
        
            Node<T> temp=curr.getNext();
            temp.setPrev(curr.getPrev());
            curr.getPrev().setNext(temp);
            curr=curr.getPrev();
            
        }else if(curr==head&&curr.getNext()!=null){
        
            head=head.getNext();
            head.setPrev(null);
            curr=head;
        
        }else if(curr.getPrev()!=null&&curr==tail){
        
            tail=tail.getPrev();
            tail.setNext(null);
            curr=tail;
        
        }
    
    }
    
    /**
    * moves the cursor to the first node in the linked list.
    * @precondition
    *  There is at least one node.
    * @postcondition
    *  The cursor points to the first node in the linked list.
    */
    public void first(){
    
        if(head==null) return;
        
        curr=head;
    
    }
    
    /**
    * moves the cursor to the last node in the linked list.
    * @precondition
    *  There is at least one node.
    * @postcondition
    *  The cursor points to the last node in the linked list.
    */
    public void last(){
    
        if(head==null) return;
        
        curr=tail;
    
    }
    
    /**
    * Sets the cursor to the next node in the inked list.
    */
    public void next(){
    
        if(curr==null) return;
        curr=curr.getNext();
    
    }
    
    /**
    * Get the data from the node the cursor points to
    * @return Data stored at cursor node.
    */
    public T getData(){
    
        if(curr!=null) return curr.getData();
        return null;
    
    }
    
    /**
    * Removes all nodes after the current node.
    * @precondition
    *  There are at least 2 nodes
    * @postcondition
    *  All nodes after the current node are destroyed.
    */
    public void removeAfter(){
    
        if(head==tail) return;//no other nodes, nothing to do.
        tail=curr.getNext();
        tail.setNext(null);
    
    }
    
    /**
    * Removes all nodes before the current node.
    * @precondition
    *  There are at least 2 nodes
    * @postcondition
    *  All nodes before the current node are destroyed.
    */
    public void removeBefore(){
    
        if(head==tail) return;//no other nodes, nothing to do.
        head=curr.getPrev();
        head.setPrev(null);
    
    }

}
