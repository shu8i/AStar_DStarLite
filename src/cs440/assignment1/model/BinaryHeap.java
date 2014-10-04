package cs440.assignment1.model;

import java.util.Comparator;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public class BinaryHeap {

    private Block[] minqueue;
    private int N;
    private Comparator<Block> comparator;
    
    public BinaryHeap(int size, Comparator<Block> comparator){
    	minqueue = new Block[size+1];
    	N = 0;
    	this.comparator = comparator;
    }
    
    public boolean isEmpty() {
        return N == 0;
    }
    
    public int size() {
        return N;
    }
    
    public boolean contains(Block b){
    	for (int i = 1; i < N+1; i++){
    		if (b.equals(minqueue[i])) return true;
    	}
    	return false;
    }
    
    public Block peek() {
        if (isEmpty()) return null;
        return minqueue[1];
    }
    
    public Block poll() {
        if (isEmpty()) return null;
        swap(1, N);
        Block first = minqueue[N];
        N--;
        minqueue[N+1] = null;
        fixRemove(1);
        if ((N > 0) && (N == (minqueue.length-1)/4)) newSize(minqueue.length/2);
        return first;
    }
    
    public void add(Block b) {
        if (N == (minqueue.length-1)) newSize(2*minqueue.length);
        
        N++;
        minqueue[N] = b;
        fixAdd(N);
    }
    
    public void remove(Block b){
    	int x = 0;
    	for (int i = 1; i < N+1; i++){
    		if (b.equals(minqueue[i])) x = i;
    	}
        if (x == 0) return;
    	swap(x, N);
    	N--;
        minqueue[N+1] = null;
        fixRemove(x);
        if ((N > 0) && (N == (minqueue.length-1)/4)) newSize(minqueue.length/2);
    }

    /* helper functions */
    
    private void newSize(int newsize){
    	if (newsize <= N) return;
    	
    	Block[] newminqueue = new Block[newsize+1];
    	for (int i = 0; i < N+1; i++){
    		newminqueue[i] = minqueue[i];
    	}
    	minqueue = newminqueue;
    }
    
    private void fixAdd(int x){
    	while (x > 1) {
    		if (compare(x/2, x)) {
                swap(x, x / 2);
                x = x / 2;
            }
            else break;
        }
    }
    
    private void fixRemove(int x){
    	 while (2*x <= N) {
             int t = 2*x;
             if (t < N && compare(t, t+1)) t++;
             if (!compare(x, t)) break;
             swap(x, t);
             x = t;
         }
    }
    
    // returns true if the block at x's f-value is greater than the f-value of the block at y
    private boolean compare(int x, int y){
    	return comparator.compare(minqueue[x], minqueue[y]) > 0;
    }
    
    private void swap(int x, int y) {
        Block t = minqueue[x];
        minqueue[x] = minqueue[y];
        minqueue[y] = t;
    }
    
}
