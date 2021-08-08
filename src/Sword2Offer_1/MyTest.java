package Sword2Offer_1;
/*
    设计一个类，我们只能生成该类的一个实例。(单例设计模式)
 */
public class MyTest {
    public static void main(String[] args) {
        User user1 = User.newInstance(1,"nxt");
        User user2 = User.newInstance(2,"waq");
        System.out.println(user1.hashCode());
        System.out.println(user2.hashCode());
    }
}
//定义User类
class User{
    //属性1
    private int id;
    //属性2
    private String username;
    //为什么使用volatile关键字：因为new一个对象的过程不是原子性的。
    //指令1：获取内存地址
    //指令2：初始化对象
    //指令3：将这块内存地址，指向引用变量singleton。
    //那么，这样我们就比较好理解，为什么要加入Volatile变量了。由于Volatile禁止JVM对指令进行重排序。
    //所以创建对象的过程仍然会按照指令1-2-3的有序执行。
    //反之，如果没有Volatile关键字，假设线程A正常创建一个实例，那么指定执行的顺序可能2-1-3，
    // 当执行到指令1的时候，线程B执行getInstance方法获取到的，可能是一个空对象
    private static volatile User user = null;
    private User(int id,String username){
        this.id = id;
        this.username = username;
    }
    public static User newInstance(int id,String username){
        //第一次校验，如果不是null，则直接返回，避免进入同步代码块，提升效率
        if(user==null){
            //加锁
            synchronized(User.class){
                //第二次判断是为了避免以下情况的发生。
                //(1)假设：线程A已经经过第一次判断，判断singleton=null，准备进入同步代码块.
                //(2)此时线程B获得时间片，犹豫线程A并没有创建实例，所以，判断singleton仍然=null，所以线程B创建了实例singleton。
                //(3)此时，线程A再次获得时间片，犹豫刚刚经过第一次判断singleton=null(不会重复判断)，
                // 进入同步代码块，这个时候，我们如果不加入第二次判断的话，那么线程A又会创造一个实例singleton，就不满足我们的单例模式的要求，
                // 所以第二次判断是很有必要的。
                if(user==null){
                    user = new User(id,username);
                }
            }
        }
        return user;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
