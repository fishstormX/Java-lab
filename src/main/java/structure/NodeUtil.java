package structure;


import org.junit.Test;

public class NodeUtil {
    /**
     *  根据数组创建Node链表并返回头节点
     *
     * */
    public static Node createLinkedNode(int []a) throws LinkedNodeLengthException {
        if(a.length==0){
            throw new LinkedNodeLengthException();
        }
        Node node=new Node(a[0]);
        Node head =node;
        for(int i=1;i<a.length;i++){
            node=setNextNode(node,new Node(a[i]));
        }
        return head;
    }
    /**
     * 打印出所有的LinkedNode元素
     * */
    public static void printLinkedNode(Node head){
        System.out.print(head.val);
        if(head.next!=null){
            System.out.print("->");
            printLinkedNode(head.next);
        }
        //System.out.println("");
    }

    private static Node setNextNode(Node node, Node next){
        node.next=next;
        return next;
    }

    @Test
    public void test() throws LinkedNodeLengthException {
        Node x=createLinkedNode(new int[]{1,2,5,8,0,123});
        printLinkedNode(x);
    }

    public static class LinkedNodeLengthException extends Exception{
    }

}
