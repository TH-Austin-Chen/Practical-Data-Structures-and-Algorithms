import java.util.ArrayList;
import java.util.List;

class IntervalST<Key extends Comparable<Key>, Value> {
    private Node root;

    private class Node {
        Key lo, hi;
        Value val;
        Key max;
        int size;
        Node left, right;

        public Node(Key lo, Key hi, Value val) {
            this.lo = lo;
            this.hi = hi;
            this.val = val;
            this.max = hi; // initially, max is the hi value of the interval itself
            this.size = 1;
        }
    }

    public IntervalST() {
        root = null;
    }

    public void put(Key lo, Key hi, Value val) {
        root = put(root, lo, hi, val);
    }

    private Node put(Node x, Key lo, Key hi, Value val) {
        // If the current node is null, create a new node with the given interval and value.
        if (x == null) return new Node(lo, hi, val);
        
        // Compare the lower bound of the new interval with the current node's lower bound.
        int cmp = lo.compareTo(x.lo);
    
        // If the new interval's lower bound is less than the current node's,
        // insert the new interval into the left subtree.
        if (cmp < 0) {
            x.left = put(x.left, lo, hi, val);
        } 
        // If the new interval's lower bound is greater than the current node's,
        // insert the new interval into the right subtree.
        else if (cmp > 0) {
            x.right = put(x.right, lo, hi, val);
        } 
        // If the lower bounds are the same, check the upper bounds.
        else {
            int cmpHi = hi.compareTo(x.hi);
            // If the upper bounds are also the same, update this node's value.
            if (cmpHi == 0) {
                x.val = val;
            } 
            // If the upper bounds differ, treat it as a new interval and
            // continue searching in the right subtree to find the appropriate
            // position to insert this new interval.
            else {
                x.right = put(x.right, lo, hi, val);
            }
        }
    
        // After insertion, update the current node's size and max properties.
        // The size is recalculated as the sum of sizes of left and right children plus one (for the current node).
        x.size = 1 + size(x.left) + size(x.right);
        // The max is the maximum of the current node's high value and the max values of the left and right subtrees.
        x.max = max(x.hi, max(getMax(x.left), getMax(x.right)));
    
        // Return the current node with updated properties.
        return x;
    }
    


    public void delete(Key lo, Key hi) {
        root = delete(root, lo, hi);
    }

    private Node delete(Node x, Key lo, Key hi) {
        if (x == null) return null;
    
        int cmp = lo.compareTo(x.lo);
        // Continue searching in the left or right subtree based on the comparison of lo values.
        if (cmp < 0) {
            x.left = delete(x.left, lo, hi);
        } else if (cmp > 0) {
            x.right = delete(x.right, lo, hi);
        } else {
            // If lo values match, compare hi values to find the exact node.
            int cmpHi = hi.compareTo(x.hi);
            if (cmpHi != 0) {
                x.right = delete(x.right, lo, hi);  // If the given hi is less, search the left subtree.
            } else {
                // This is the node to delete (both lo and hi match).
                if (x.left == null) return x.right;
                if (x.right == null) return x.left;
    
                // Node with two children: Get the smallest node from the right subtree.
                Node t = x;
                Node minNode = min(t.right);
                x.lo = minNode.lo;
                x.hi = minNode.hi;
                x.val = minNode.val;
                x.right = deleteMin(t.right);  // Delete the minimum node from the right subtree.
            }
        }
    
        // After deletion, update the current node's size and max.
        return update(x);
    }
    
    // The rest of the helper functions (min, deleteMin, update) remain unchanged.
    
    
    private Node min(Node x) {
        while (x.left != null) {
            x = x.left;
        }
        return x;
    }
    
    private Node deleteMin(Node x) {
        if (x.left == null) {
            return x.right;  // No left child, right child takes its place
        }
        x.left = deleteMin(x.left);
        return update(x);  // Update this node after recursive deleteMin
    }
    
    private Node update(Node x) {
        if (x != null) {
            x.size = 1 + size(x.left) + size(x.right);
            x.max = max(x.hi, max(getMax(x.left), getMax(x.right)));
        }
        return x;
    }
    
    private Key max(Key a, Key b) {
        if (a == null) return b;
        if (b == null) return a;
        return (a.compareTo(b) > 0) ? a : b;
    }
    
    private Key getMax(Node x) {
        return (x == null) ? null : x.max;
    }
    
    private int size(Node x) {
        return (x != null) ? x.size : 0;
    }
    
    public List<Value> intersects(Key lo, Key hi) {
        List<Value> result = new ArrayList<>();
        intersects(root, lo, hi, result);
        return result;
    }
    
    private void intersects(Node x, Key lo, Key hi, List<Value> result) {
        if (x == null) return;
        
        // Check intersection with the left subtree if relevant
        if (x.left != null && x.left.max.compareTo(lo) >= 0) {
            intersects(x.left, lo, hi, result);
        }
    
        // Check current node's interval for intersection
        if (isIntersecting(x.lo, x.hi, lo, hi)) {
            result.add(x.val);
        }
    
        // Check intersection with the right subtree
        // Traverse the right subtree if there's a chance it contains intervals that intersect with [lo, hi]
        if (x.right != null && x.right.max.compareTo(lo) >= 0 && x.lo.compareTo(hi) <= 0) {
            intersects(x.right, lo, hi, result);
        }
    }   
    
    private boolean isIntersecting(Key lo1, Key hi1, Key lo2, Key hi2) {
        return lo1.compareTo(hi2) <= 0 && hi1.compareTo(lo2) >= 0;
    }


    public static void main(String[] args) {
        IntervalST<Integer, String> IST = new IntervalST<>();
        IST.put(2, 5, "badminton");
        IST.put(1, 5, "PDSA HW7");
        IST.put(3, 5, "Lunch");
        IST.put(3, 6, "Workout");
        IST.put(3, 7, "Do nothing");
        IST.delete(2, 5); // delete "badminton"
        System.out.println(IST.intersects(1, 2));
        
        IST.put(8, 8, "Dinner");
        System.out.println(IST.intersects(6, 10));
        
        IST.put(3, 7, "Do something"); // Update value
        System.out.println(IST.intersects(7, 7));
        
        IST.delete(3, 7); // delete "Do something"
        System.out.println(IST.intersects(7, 7));
    }
}
