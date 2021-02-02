package p1;

class LinkedList<T> {
    private int numberOfNodes = 0; 
    private ListNode<T> front = null;
    private ListNode<T> back = null;
    private ListNode<T> temp = null;
    // Returns true if the linked list has no nodes, or false otherwise.
    public boolean isEmpty() {
        return (front == null);
    }
    
    // Deletes all of the nodes in the linked list.
    // Note: ListNode objects will be automatically garbage collected by JVM.
    public void makeEmpty() {
        front = null;
        numberOfNodes = 0;
    }
    
 public void addBack( T element ) {
        temp = front;
        if (isEmpty()){
            addFront(element);
        }
        else{
        while(temp.next!=null){
            temp=temp.next;
        }
        temp.next=new ListNode<T>(element, null);
        numberOfNodes++;
        }
    }
    
    // Adds a node to the front of the linked list.
    public void addFront( T element ) {
        front = new ListNode<T>( element, front);
        numberOfNodes++;
    }
    public T removeEnd(){
        
        if(front.next == null) {
            ListNode<T> current = front;
            while (current.next != null)
                current = current.next;
            T result = current.getData();
            current.next = null;
            return result;
        }
        
        ListNode<T> position = front;
        ListNode<T> temp = front;   //temp has to be initialized to something 
        T dataAtEnd = null;
        while (position != null)
        {   dataAtEnd = position.data;    
            temp = position;               //safe keep current position
            position = position.next;     //update position pointer to get the next value  
        }
        
        position =temp;  // store current position in next position
        return dataAtEnd;
    }
    
    // Returns a reference to the data in the first node, or null if the list is empty.
    public T first() {
        if (isEmpty()) 
            return null;
        
        return front.getData();
    }
// Removes a node from the front of the linked list (if there is one).
// Returns a reference to the data in the first node, or null if the list is empty.
    @SuppressWarnings("unchecked")
    public T removeFront() {
        T tempData;
        
        if (isEmpty()) 
            return null;
        
        tempData = front.getData();
        front = front.getNext();
        numberOfNodes--;
        return tempData;
    }
// Returns true if the linked list contains a certain element, or false otherwise.
    @SuppressWarnings("unchecked")
    public boolean contains( T key ) {
        ListNode<T> searchNode;
        searchNode = front;
        while ( (searchNode != null) && (!key.equals(searchNode.getData())) ) {
            searchNode = searchNode.getNext();
        }
        return (searchNode != null);
    }
// Return String representation of the linked list.
    @SuppressWarnings("unchecked")
    public String toString() {
        ListNode<T> node;
        String linkedList = "FRONT ==> ";
        
        node = front;
        while (node != null) {
            linkedList += "[ " + node.getData() + " ] ==> ";
            node = node.getNext();
        }
        
        return linkedList + "NULL";
    }
    // Insert a node in the list with a given key value
    @SuppressWarnings("unchecked")
    public void insert( Comparable key ) {
        ListNode<T> before = null;
        ListNode<T> after = front;
        ListNode<T> newNode;        
        
        // Traverse the list to find the ListNode before and after our insertion point.
        while ((after != null) && (key.compareTo(after.getData()) > 0)) {
            before = after;
            after = after.getNext();
        }
        
        // Create the new node with link pointing to after
        newNode = new ListNode<T>( (T)key, after);
        
        // Adjust front of the list or set before's link to point to new node, as appropriate
        if (before == null) {
            front = newNode;
        }
        else {
            before.setNext(newNode);
        }
        numberOfNodes++;
    }
    // Returns the number of nodes in the linked list
    public int size() {
        return numberOfNodes;
    }
}
