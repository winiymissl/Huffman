import java.io.*;
import java.util.*;

/**
 * @Description 将文章转化为HuffmanCodes
 * @Author winiymissl
 * @Date 2023-11-07
 */

class Node implements Comparable<Node> {
    private static Map<Character, Integer> map;
    private static Node n = n = new Node();
    private char character;
    private int frequency;
    private Node left;
    private Node right;
    private Node parent;
    public Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public Node() {
    }

    public static void main(String[] args) {
        //得到map
        map = n.getWeights(n.getPage());
        //层序遍历哈夫曼树
        System.out.println(map);
        //打印哈夫曼树
        printTree(n.getRootNode(map));
        //进行哈夫曼编码
        Map<Character, String> table = new HashMap<>();
        n.enCodes(n.getRootNode(map), new String(), table);
        //对应哈夫曼树将其变成哈夫曼编码存储在CodeFile中
        String code = new String();
        for (char i : n.getPage().toCharArray()) {
            code += table.get(i);
        }
        n.codeToFile(code);
        //输出每一个字符对应的哈夫曼编码
        //System.out.println(table);
        //译码
        System.out.println(decode(code));
    }

    public static void printTree(Node root) {
        printTree(root, "", true);
    }

    private static void printTree(Node node, String prefix, boolean isTail) {
        if (node != null) {
            System.out.println(prefix + (isTail ? "└── " : "├── ") + node.getFrequency());
            if (node.left != null || node.right != null) {
                printTree(node.left, prefix + (isTail ? "    " : "│   "), false);
                printTree(node.right, prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }

    public static String decode(String encodedData) {
        StringBuilder decodedData = new StringBuilder();
        Node root = n.getRootNode(map);
        Node current = root;

        for (char bit : encodedData.toCharArray()) {
            if (bit == '0') {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.left == null && current.right == null) {
                // 到达叶子节点，找到对应的原始字符
                decodedData.append(current.character);
                current = root; // 从根节点重新开始
            }
        }
        return decodedData.toString();
    }

    private void codeToFile(String code) {
        File codeFile = new File("CodeFile.txt");
        try {
            FileWriter fileWriter = new FileWriter(codeFile);
            fileWriter.write(code);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("getCode: " + e);
        }
    }

    private void enCodes(Node node, String str, Map<Character, String> map) {
        if (node == null) return;
        if (node.left == null && node.right == null) {
            map.put(node.getCharacter(), str);
        } else {
            enCodes(node.left, str + "0", map);
            enCodes(node.right, str + "1", map);
        }
    }

    private Node getRootNode(Map<Character, Integer> map) {
        PriorityQueue<Node> minHeap = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            minHeap.add(new Node(entry.getKey(), entry.getValue()));
        }
        // 构建哈夫曼树
        while (minHeap.size() > 1) {
            Node left = minHeap.poll();
            Node right = minHeap.poll();
            Node parent = new Node('\0', left.frequency + right.frequency);
            parent.setLeft(left);
            parent.setRight(right);
            minHeap.add(parent);
        }
        // 返回根节点，这就是哈夫曼树
        return minHeap.poll();
    }

    private Map getWeights(String string) {
        Map<Character, Integer> charWeights = new HashMap<>();
        for (char c : string.toCharArray()) {
            if (charWeights.containsKey(c)) {
                charWeights.put(c, charWeights.get(c) + 1);
            } else {
                charWeights.put(c, 1);
            }
        }
        // 打印字符和权值
        for (Map.Entry<Character, Integer> entry : charWeights.entrySet()) {
            System.out.println("Character: " + entry.getKey() + ", Weight: " + entry.getValue());
        }
        return charWeights;
    }

    private String getPage() {
        String ans = null;
        try {
            File path = new File("pager.txt");
            FileReader fileReader = null;
            fileReader = new FileReader(path);
            char[] data = new char[999999];
            int len = 0;
            ans = new String();
            while ((len = fileReader.read(data)) != -1) {
                ans += String.valueOf(data, 0, len);
            }
            System.out.println("getPage : " + ans);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ans;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public int compareTo(Node other) {
        return this.frequency - other.frequency;
    }
}