package one;

/**
 * @author lost
 */
public class Partition {
    private int start;
    private int size;
    private String owner;
    
    public Partition(int start,int size) {
        this.size = size;
        this.start = start;
        owner = "Empty";
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getStart() {
        return start;
    }
    
    public void setStart(int start) {
        this.start = start;
    }
    
    @Override
    public String toString() {
        return "Partition{" +
                       "首址=" + start +
                       ", 大小=" + size +
                       ", 进程='" + owner + '\'' +
                       '}';
    }
}
