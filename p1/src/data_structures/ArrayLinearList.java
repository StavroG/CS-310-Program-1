package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayLinearList<E> implements LinearListADT<E>{
	final int MAX_CAPACITY;
	int size, front, end, modCounter = 0;
	E[] list;
	
	@SuppressWarnings("unchecked")
	public ArrayLinearList() {
		MAX_CAPACITY = DEFAULT_MAX_CAPACITY;
		list = (E[]) new Object[DEFAULT_MAX_CAPACITY];
	}
	
	@SuppressWarnings("unchecked")
	public ArrayLinearList(Integer max) {
		MAX_CAPACITY = max;
		list = (E[]) new Object[max]; 
	}
	
	@Override
	public void ends() {
		System.out.println("Front: " + list[front] + " End: " + list[end]);
	}

	@Override
	public boolean addFirst(E obj) {
		if (isFull()) {
			return false;
		}
		
		front--; 			//Move the front position
		if (front == -1) {	//If the front goes to -1, which is not a valid index in the array, loops it to the other side of the array.
			front = list.length - 1;
		}
		
		if (isEmpty()) {	//If the list is empty set end to front.
			end = front;
		}
		list[front] = obj;
		
		size++;
		modCounter++;
		return true;
	}

	@Override
	public boolean addLast(E obj) {
		if (isFull()) {
			return false;
		}
		
		end++;				//Move the end position. 
		if (end == MAX_CAPACITY) {//If the end goes to the max, which is not a valid index in the array, loops it to the other side of the array.
			end = 0;
		}
		
		if (isEmpty()) {	//If empty set front to end.
			front = end;
		}
		list[end] = obj;
		
		size++;
		modCounter++;
		return true;
	}

	@Override
	public E removeFirst() {
		if (isEmpty()) {	//If empty there is nothing to remove.
			return null;
		}
		
		E tmp = list[front];	//Stores the value of the first element then removes the first element from the list.
		list[front] = null;
		front++;	//Need to change the front because we removed the old one
		
		if (front == MAX_CAPACITY) {
			front = 0;
		}
		
		size--;
		modCounter++;
		return tmp;
	}

	@Override
	public E removeLast() {
		if (isEmpty()) {	//If empty there is nothing to remove.
			return null;
		}
		
		E tmp = list[end];	//Stores the value of the last element then removes the last element from the list.
		list[end] = null;	
		end--;				//Need to change the end because we removed the old one
		
		if (end == -1) {	
			end = list.length - 1;
		}
		
		size--;
		modCounter++;
		return tmp;
	}

	@Override
	public E remove(E obj) {
		if (list[front] != null) {			//Checks if the front element is the one we are searching for.
			if (list[front].equals(obj)) {	//If it is then we delete the front element and make the next element in the list front.
				list[front] = null;
				front++;
				if (front == MAX_CAPACITY) {//Makes sure that front is not outside of the list.
					front = 0;
				}
				size--;
				modCounter++;
				return obj;
			}
		}
		
		if (list[end] != null) {		//Checks if the end element is the one we are searching for.
			if (list[end].equals(obj)) {
				list[end] = null;
				end--;
				if (end == -1) {
					end = list.length - 1;
				}
				size--;
				modCounter++;
				return obj;
			}
		}
		
		int search = front;					//If the obj we are searching for is not the front or the end of the list then we search through the rest of the list.
		for (int i = 0; i < list.length; i++) {
			if (search == MAX_CAPACITY) {
				search = 0;
			}
			
			if (list[search] != null) {
				if (list[search].equals(obj)) {
					list[search] = null;
					while(true) {			//We need to check the elements in the list.
						if (search - 1 == -1) {
							search = list.length - 1;
						}
						
						if (list[search - 1] != null) {
							E tmp = list[search - 1];
							list[search - 1] = null;
							list[search] = tmp;
							search--;
							continue;
						}
						else {
							size--;
							modCounter++;
							front++;
							return obj;
						}
					}
				}
			}
			search++;
		}
		return null;
	}
	
	@Override
	public E peekFirst() {
		if (isEmpty()) {
			return null;
		}
		return list[front];
	}

	@Override
	public E peekLast() {
		if (isEmpty()) {
			return null;
		}	
		return list[end];
	}

	@Override
	public boolean contains(E obj) {
		if (find(obj) != null) {
			return true;
		}
		return false;
	}

	@Override
	public E find(E obj) {
		int search = front;

		for (int i = 0; i < list.length; i++) {		
			if (search == MAX_CAPACITY) {	//If we go to the max capacity we need to loop back around.
				search = 0;
			}
			
			if (list[search] != null) {		//Need to make sure that the element in the list at index search is not null so we don't get an error.
				if (list[search].equals(obj)) {	//Check to see if the element in list at search is the element we are looking for.
					return list[search];
				}
			}
			search++;
		}
		return null;
	}
	
	@Override
	public void clear() {
		for (int i = 0; i < list.length; i++) {	//Need to loop through the list to set everything to null.	
			list[i] = null;						//If we just set list to null that will remove the size of the list.
		}
		size = 0;
		front = 0;
		end = front;
	}

	@Override
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isFull() {
		if (size == MAX_CAPACITY) {
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<E> iterator() {
		return new IteratorHelper();
	}

	class IteratorHelper implements Iterator<E>{
		int index;
		int changeCount;
		
		public IteratorHelper() {
			index = 0;
			changeCount = modCounter;
		}
		
		@Override
		public boolean hasNext() {
			if (changeCount != modCounter) {
				throw new ConcurrentModificationException();
			}
			return (index < size);
		}

		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return list[index++];
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
