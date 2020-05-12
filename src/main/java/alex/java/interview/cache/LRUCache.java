package alex.java.interview.cache;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Objects;

/**
 * LRU算法
 *
 * @author shenjiangang
 * @date 2020/05/11
 */
public class LRUCache {

    /**
     * 缓存数据
     */
    private HashMap<Integer, Node> cache;

    /**
     * 头结点，最近使用节点
     */
    private Node head;

    /**
     * 尾节点，最近最少使用节点(即将淘汰节点)
     */
    private Node tail;

    /**
     * 最大容量
     */
    private int capacity;

    /**
     * 当前容量
     */
    private int size = 0;

    public LRUCache(int capacity) {
        cache = new HashMap(capacity);
        this.capacity = capacity;
    }

    /**
     * 获取缓存中的数据
     *
     * @param key
     *
     * @return
     */
    public int get(Integer key) {
        if (!cache.containsKey(key)) {
            return -1;
        }
        Node node = cache.get(key);
        refresh(node);
        return node.value;
    }

    /**
     * 刷新被访问节点的位置
     *
     * @param node 被访问节点
     */
    private void refresh(Node node) {
        // 头点无需处理
        if (Objects.equals(node, head)) {
            return;
        }
        delete(node);
        add(node);
    }

    /**
     * 头部插入最新数据
     *
     * @param node
     */
    private void add(Node node) {
        if (Objects.equals(head, null) && Objects.equals(tail, null)) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.pre = node;
            head = node;
        }
        cache.put(node.key, node);
    }


    /**
     * 删除节点
     *
     * @param node 被删除的节点
     */
    private void delete(Node node) {
        // 只有一个节点时
        if (Objects.equals(head, tail)) {
            head = null;
            tail = null;
            return;
        }

        // 删除头结点
        if (Objects.equals(node, head)) {
            head = head.next;
            head.pre = null;
            return;
        }

        // 删除尾节点
        if (Objects.equals(node, tail)) {
            tail = tail.pre;
            tail.next = null;
            return;
        }

        // 删除中间节点
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }


    private void put(Integer key, Integer value) {
        if (Objects.isNull(key) || Objects.isNull(value)) {
            return;
        }

        // 已存在,更新数据
        if (cache.containsKey(key)) {
            cache.get(key).value = value;
            refresh(cache.get(key));
            return;
        }

        Node node = new Node(key, value);
        if (size >= capacity) {
            cache.remove(tail.key);
            delete(tail);
        } else {
            size++;
        }
        add(node);

    }

    /**
     * 双向链表存储缓存数据
     */
    private class Node {
        private Node pre;
        private Node next;
        private int value;
        private int key;

        public Node getPre() {
            return pre;
        }

        public void setPre(Node pre) {
            this.pre = pre;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2 /* 缓存容量 */);

        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));      // 返回  1
        cache.put(3, 3);    // 该操作会使得密钥 2 作废
        System.out.println(cache.get(2));       // 返回 -1 (未找到)
        cache.put(4, 4);    // 该操作会使得密钥 1 作废
        System.out.println(cache.get(1));       // 返回 -1 (未找到)
        System.out.println(cache.get(3));       // 返回  3
        System.out.println(cache.get(4));      // 返回  4
    }

}
