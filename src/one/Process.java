package one;

/**
 * @author lost
 */
public class Process{
    private final int size;
    public Process(int size){
        this.size = size;
    }
    public synchronized int getSize(){
        return size;
    }
}