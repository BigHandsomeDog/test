package one;

import one.Partition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author lost
 */
public class DynamicPartition {

    public static void main(String[] args) {
        int initSize;
        int amount;
        DynamicPartition dynamicPartition = new DynamicPartition();
        Scanner scan = new Scanner(System.in);
        System.out.print("请设置内存大小:");
        initSize = scan.nextInt();
        dynamicPartition.init(initSize);
        dynamicPartition.test();
        dynamicPartition.showEmpty();
        dynamicPartition.showMember();
        System.out.print("请输入进程数:");
        amount = scan.nextInt();
        for (int i = 0; i < amount; i++) {
            String nameProcess;
            int sizeProcess;
            System.out.print("请输入进程名:");
            nameProcess = scan.next();
            System.out.print("请输入进程大小:");
            sizeProcess = scan.nextInt();
            requestList.put(nameProcess, new Process(sizeProcess));
        }
        int command = 0;
        do {
            menuAlgorithm();
            System.out.println("请输入算法序列号:");
            command = scan.nextInt();
            switch (command) {
                case 0:
                    break;
                case 1:
                    System.out.println("----------最先适应算法----------");
                    dynamicPartition.theFirstFit();
                    break;
                case 2:
                    System.out.println("----------最佳适应算法----------");
                    dynamicPartition.theBestFit();
                    break;
                case 3:
                    System.out.println("----------最坏适应算法----------");
                    dynamicPartition.theWorstFit();
                    break;
                default:
                    System.out.println("请重新输入！！！");
            }
        } while (command != 0);
        scan.close();
    }

    protected ArrayList<Partition> emptyList,dynamicList;

    protected static HashMap<String, Process> requestList;

    protected Comparator<Partition> arrangeAscend;

    public DynamicPartition() {
        arrangeAscend = new Comparator<Partition>() {
            @Override
            public int compare(Partition o1, Partition o2) {
                return o1.getStart() - o2.getStart();
            }
        };
        dynamicList = new ArrayList<>();
        emptyList = new ArrayList<>();
        requestList = new HashMap<>();
    }

    public void init(int initSize) {
        Partition temp = new Partition(0, initSize);
        emptyList.add(temp);
        dynamicList.add(temp);
    }

    /**
     * 操作菜单
     */
    public void menuOperate() {
        System.out.println("----------请选择以下操作----------");
        System.out.println("           0.退出            ");
        System.out.println("           1.申请进程            ");
        System.out.println("           2.释放进程            ");
        System.out.println("           3.添加进程            ");
    }

    /**
     * 算法菜单
     */
    public static void menuAlgorithm() {
        System.out.println("----------请选择分区算法----------");
        System.out.println("           0.退出            ");
        System.out.println("           1.最先适应算法         ");
        System.out.println("           2.最佳适应算法         ");
        System.out.println("           3.最坏适应算法         ");
    }

    /**
     * 根据进程名判断是否分配进程
     */
    public boolean distribute(String key) {
        int size = 0;
        Partition part;
        boolean result = true;
        if (requestList.containsKey(key)) {
            size = requestList.get(key).getSize();

            for (int i = 0; i < emptyList.size(); i++) {
                part = emptyList.get(i);
                if (size > part.getSize()) {
                    result = false;
                    continue;
                } else if (size == part.getSize()) {
                    part.setOwner(key);
                    emptyList.remove(part);
                    requestList.remove(key);
                    result = true;
                    break;
                } else {
                    Partition part1 = new Partition(part.getStart(), size);
                    Partition part2 = new Partition(part.getStart() + size, part.getSize() - size);
                    part1.setOwner(key);
                    requestList.remove(key);
                    emptyList.remove(part);
                    dynamicList.remove(part);
                    emptyList.add(part2);
                    dynamicList.add(part1);
                    dynamicList.add(part2);
                    result = true;
                    break;
                }
            }
        } else {
            System.out.println("该进程不在等待序列！！！");
            result = false;
        }

        return result;
    }

    public void test(){
        String a = "1";
        String b = "5";
        String c = "2";
        String d = "6";
        String e = "3";
        String f = "7";
        String g = "4";
        requestList.put("1", new Process(50));
        requestList.put("5", new Process(30));
        requestList.put("2", new Process(30));
        requestList.put("6", new Process(50));
        requestList.put("3", new Process(40));
        requestList.put("7", new Process(40));
        requestList.put("4", new Process(50));
        distribute(a);
        distribute(b);
        distribute(c);
        distribute(d);
        distribute(e);
        distribute(f);
        distribute(g);
        finish(b);
        finish(d);
        finish(f);
    }
    public void finish(String name) {
        Iterator<Partition> iterator = dynamicList.iterator();
        while (iterator.hasNext()) {
            Partition part = iterator.next();
            if (part.getOwner().equals(name)) {
                part.setOwner("Empty");
                emptyList.add(part);
                requestList.put(name, new Process(part.getSize()));
                break;
            }
        }
    }

    /**
     * 算法实现
     */
    public void algorithm(Comparator<Partition> compare) {
        Scanner input = new Scanner(System.in);
        int command = 0;
        do {
            menuOperate();
            command = input.nextInt();
            switch (command) {
                case 0:
                    break;
                case 1:
                    System.out.print("进程名:");
                    if (distribute(input.next())) {
                        System.out.println("分配成功！！！");
                    } else {
                        System.out.println("分配失败！！！");
                    }
                    emptyList.sort(compare);
                    dynamicList.sort(arrangeAscend);
                    showEmpty();
                    showMember();
                    break;
                case 2:
                    System.out.print("进程名:");
                    finish(input.next());
                    emptyList.sort(compare);
                    while (merge()) {
                        emptyList.sort(compare);
                    }
                    dynamicList.sort(arrangeAscend);
                    showEmpty();
                    showMember();
                    break;
                case 3:
                    System.out.print("进程名:");
                    String temp = input.next();
                    if (requestList.containsKey(temp)) {
                        System.out.println("进程名重复，添加失败！！！");
                    } else {
                        System.out.print("进程大小:");
                        requestList.put(temp, new Process(input.nextInt()));
                    }
                    break;
                default:
                    System.out.println("请重新输入！！！");
            }
        } while (command != 0);
    }

    /**
     *将两个相邻的空闲分区合并为一个
     */
    public boolean merge() {
        int i = 0;
        boolean result = false;

        ArrayList<Partition> copy = new ArrayList<>(emptyList);
        copy.sort(arrangeAscend);
        for (; i < copy.size() - 1; i++) {
            Partition temp = copy.get(i);

            if ((temp.getStart() + temp.getSize()) == copy.get(i + 1).getStart()) {
                Partition combine = new Partition(temp.getStart(), temp.getSize() + copy.get(i + 1).getSize());
                emptyList.remove(copy.get(i));
                emptyList.remove(copy.get(i + 1));
                dynamicList.remove(copy.get(i));
                dynamicList.remove(copy.get(i + 1));
                emptyList.add(combine);
                dynamicList.add(combine);
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 显示内存占用情况
     */
    public void showMember() {
        System.out.println("----------内存情况----------");
        Iterator<Partition> iterator = dynamicList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     *空闲分区
     */
    public void showEmpty() {
        System.out.println("----------空闲列表----------");
        for (Partition part : emptyList) {
            System.out.println(part);
        }
    }

    /**
     * 最先适应算法
     */
    public void theFirstFit() {
        emptyList.sort(arrangeAscend);
        algorithm(arrangeAscend);
    }

    /**
     * 最佳适应算法
     */
    public void theBestFit() {
        Comparator<Partition> sizeAscend = new Comparator<Partition>() {
            @Override
            public int compare(Partition o1, Partition o2) {
                return o1.getSize() -
                        o2.getSize();
            }
        };
        emptyList.sort(sizeAscend);
        algorithm(sizeAscend);
    }

    /**
     * 最坏适应算法
     */
    public void theWorstFit() {
        Comparator<Partition> sizeDescend = new Comparator<Partition>() {
            @Override
            public int compare(Partition o1,Partition o2){
                return o2.getSize() - o1.getSize();
            }
        };
        emptyList.sort(sizeDescend);
        algorithm(sizeDescend);
    }



}