import java.util.ArrayList;


/*
 * Name: Kartikeya Kaushal
 * CS 201 Project 4: ServiceQueue
 * Instructor: John Lillis
 */

//add commits

public class ServiceQueue  {

	private int maxID = 0;  //max number of elements been in queue
	private int poolSize;
	private DLLNode first, last; // front and back of queue
	private ArrayList <DLLNode> secondary = new ArrayList <DLLNode>();
	private ArrayList <Integer> freePool  = new ArrayList <Integer>();
	
	/**private class for a doubly-linked list node**/
	private class DLLNode  {
		private int ID;
		private DLLNode next, prev;
		public DLLNode()
		{
			next = null;
			prev = null;
		}
		public DLLNode(int el)
		{
			ID = el;
			next = null;
			prev = null;	
		}
		public DLLNode(int el, DLLNode n, DLLNode p)
		{
			ID = el;
			next = n;
			prev = p;
		}
	}

    /** creates an empty queue.
    */
    public ServiceQueue(){
    	first = last = null;

    }
    
    //check if queue is empty
    public boolean isEmpty(){
    	return first == null;
    }
    
    				/**************** Methods *********************/
 
    public int enqueue() {
    	//make a node
    	DLLNode x;
    	//check if a number was removed and add it back
    	if(! freePool.isEmpty())
    	{
    		x = secondary.get(freePool.get(poolSize - 1));
    		freePool.remove(poolSize - 1);
    		poolSize--;
    	
    		if(last != null)
    		{
    			DLLNode temp = last;
    			last.next = x;
    			last = x;
    			last.prev = temp;
    		}
    		else
    		{
    			first = last = x;
    		}
    }//end check freePool
    	else                     // make a new node
    	{
    		if(last != null)
    		{
    			last = new DLLNode(maxID, null, last);
    			last.prev.next = last;
    			
    		}
    		else
    			first = last = new DLLNode(maxID);
    			maxID++;
    			secondary.add(last);
    	}

        return last.ID;
    }


    public int dequeue() {
    	//check if list is already empty
    	if(isEmpty())
    		return -1;
    	//if not empty remove first element
    	int temp = first.ID;
    		if(first == last)
    		{
    			first = last = null;
    		}
    		else
    		{
    			DLLNode holder = first;
    			first = first.next;
    			first.prev = null;
    			holder.next = holder.prev = null;
    		}
    	//add node value to freePool
    	freePool.add(temp);
    	poolSize++;
    	return temp;
    	
    }

    public boolean remove(int ticketId) {
    	if(isEmpty())
            return false;
    	DLLNode temp;
    	if(ticketId < maxID && ticketId >= 0)
    	{
    		temp = secondary.get(ticketId);
    		//if only one element
    		if(temp == first && first == last)
    		{
    			first = last = null;
    		}
    		//if ticketId is the first element
    		else if(temp == first)
    		{
    			first = first.next;
    			first.prev = null;
    			temp.prev = temp.next = null;
    		}
    		//if ticketId is the last element
    		else if(temp == last)
    		{

    			last = last.prev;
    			System.out.println(last);
    			last.next = null;
    			temp.prev = temp.next = null;
    	    	System.out.println(temp.next);
    		}
    		//if ticketId is somewhere in the middle
    		else
    		{
    			if(temp.next !=null)
    				temp.next.prev = temp.prev;
    			if(temp.prev !=null)
    				temp.prev.next = temp.next;
    			temp.prev = temp.next = null;
    		}
    	freePool.add(ticketId);
	    poolSize++;
		return true;
    }
   return false;
}

    
    /** Moves ticket holder to front of line.
     if ticketId not in queue, false is returned.
     otherwise, the entry is moved to the front of the line
       and true is returned.
     if ticket is already at the front of the line, nothing happens,
       but true is still returned.
     Runtime:  O(1)
    */
    public boolean promoteToFront(int ticketId) {
    	if(ticketId == first.ID)
    		return true;
    	DLLNode temp, temp2;
    	if(ticketId < maxID && ticketId >= 0)
    	{
    		temp = secondary.get(ticketId);
			temp2 = first;

    		if(temp == last)
    		{
    			last = last.prev;
    			last.next = null;
    			first.prev = temp;
    			first = temp;
    			first.next = temp2;
    			return true;
    			
    		}
    		//if ticketId is somewhere in the middle
    		else if(temp.next != null)
    		{
    			if(temp.next !=null)
    				temp.next.prev = temp.prev;
    			if(temp.prev !=null)
    				temp.prev.next = temp.next;
    			first.prev = temp;
    			first = temp;
    			first.next = temp2;
    			return true;
    		}
    	}
        return false;
    }

    public boolean promoteOneSpot(int ticketId) {
    	//if already at front
    	if(ticketId == first.ID)
    		return true;
    	DLLNode temp, temp2, temp3, temp4;
    	if(ticketId < maxID && ticketId >= 0)
    	{
    		temp = secondary.get(ticketId);
    		temp2 = temp.prev;
    		//promote last to first
    		if(temp == last && temp2 == first)
    		{
    			promoteToFront(ticketId);
    			return true;
    		}
    		//promote to First
    		else if(temp2 == first)
    		{
    			promoteToFront(ticketId);
    			return true;
    			
    		}
    		//promote from end of line
    		else if (temp == last)
    		{
    			if(temp2.next !=null)
    				temp2.next.prev = temp2.prev;
    			if(temp2.prev !=null)
    				temp2.prev.next = temp2.next;
    			last = temp;
            	last.next = temp2;
            	last = temp2;
            	last.prev = temp;
            	return true;
    		}
    		else if( temp == first.next)
    		{
    			promoteToFront(ticketId);
    			return true;
    		}
    		//promote from anywhere else
    		else if(temp.next != null || temp.prev != null)
    		{
    			temp3 = temp2.prev;
    			temp4 = temp.next;
    			temp.prev = temp3;
    			temp4.prev = temp2;
    			temp3.next = temp;
    			temp.next = null;
    			temp2.next = temp4;
    			temp.next = temp2;
    			temp2.prev = temp;

    			return true;

    		}
    	}
        return false;
    }

    public boolean demoteToEnd(int ticketId) {
    	if(ticketId == last.ID)
    		return true;
    	DLLNode temp, temp2;
    	if(ticketId < maxID && ticketId >= 0)
    	{
    		temp = secondary.get(ticketId);
			temp2 = last;

    		if(temp == first)
    		{
    			first = first.next;
    			first.prev = null;
    			last.next = temp;
    			last = temp;
    			last.prev = temp2;
    			return true;
    			
    		}
    		//if ticketId is somewhere in the middle
    		else if(temp.prev != null)
    		{
    			if(temp.next !=null)
    				temp.next.prev = temp.prev;
    			if(temp.prev !=null)
    				temp.prev.next = temp.next;
    			last.next = temp;
    			last = temp;
    			last.prev = temp2;
    			return true;


    		}
    	}
        return false;
    }
    
    public boolean demoteOneSpot(int ticketId) {
    	if(ticketId == last.ID)
    		return true;
    	DLLNode temp, temp2, temp3, temp4;
    	if(ticketId < maxID && ticketId >= 0)
    	{
    		temp = secondary.get(ticketId);
    		temp2 = temp.next;
    		//promote last to first
    		if(temp == first && temp2 == last)
    		{
    			demoteToEnd(ticketId);
    			return true;
    		}
    		//promote to First
    		else if(temp2 == last)
    		{
    			demoteToEnd(ticketId);
    			return true;

    		}
    		//promote from end of line
    		else if (temp == first)
    		{
    			int before = temp2.ID;
    			if(temp2.next !=null)
    				temp2.next.prev = temp2.prev;
    			if(temp2.prev !=null)
    				temp2.prev.next = temp2.next;
    			first = temp;
            	first.prev = temp2;
            	first = temp2;
            	first.next = temp;
    			return true;

    		}
    		else if( temp == last.prev)
    		{
    			demoteToEnd(ticketId);
    			return true;
    		}
    		//promote from anywhere else
    		else if(temp.next != null || temp.prev != null)
    		{
    			temp3 = temp2.next;
    			temp4 = temp.prev;
    			temp.next = temp3;
    			temp3.prev = temp;
    			temp4.next = null;
    			temp4.next = temp2;
    			temp2.prev = temp4;
    			temp2.next = temp;
    			temp.prev = temp2;    			
    			return true;

    		}
    	}
        return false;
    }

    public void dump(){
    	if(isEmpty()) return;
    	
    	//prevent infinite loop. 
    	last.next = null;
    	
    	for(DLLNode temp = first; temp != null; temp = temp.next)
    	{
    		System.out.print(temp.ID + "  ");
    	}
    }
}//end ServiceQueue
