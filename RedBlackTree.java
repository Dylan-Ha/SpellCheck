package cs146F20.Ha.project4;

public class RedBlackTree<Key extends Comparable<Key>> {

	private RedBlackTree.Node<String> root;

	public static class Node<Key extends Comparable<Key>> { 
		
		Node<String> parent;
		Node<String> leftChild;
		Node<String> rightChild;
		Key key;
		boolean red;
		int color;
		
		//Node object that holds data
		public Node(Key data) {
			leftChild = null;
			rightChild = null;
			this.key = data;
		}
		
		//compares nodes to each other
		public int compareTo(Node<Key> node) { 
			//will return a number < 0 if <
			//will return number > 0 if >
			//will return 0 if =
			return key.compareTo(node.key);  
		}

		public boolean leaf() {
			//returns true if there is no left and right child
			if (this.leftChild == null && this.rightChild == null) {
				return true;
			}
			//otherwise return false
			return false;
		}
	}

	public boolean leaf(RedBlackTree.Node<String> node) {
		//returns true if there is no left and right child
		if (node.leftChild == null && node.rightChild == null) {
			return true;
		}
		//otherwise return false
		return false;
	}

	public interface Visitor<Key extends Comparable<Key>> {
		/**
		 * This method is called at each node.
		 * 
		 * @param n the visited node
		 */
		void visit(Node<Key> n);
	}

	public void preOrderVisit(Visitor<String> v) {
		preOrderVisit(root, v);
	}

	private static void preOrderVisit(RedBlackTree.Node<String> n, Visitor<String> v) {
		if (n == null) {
			return;
		}
		v.visit(n);
		preOrderVisit(n.leftChild, v);
		preOrderVisit(n.rightChild, v);
	}
	
	//visit node at key n
	public void visit(Node<Key> node) {
		//prints key of node visited
		System.out.println(node.key);
	}
	
	//prints whole tree
	public void printTree() { 
		//sets current node to root
		RedBlackTree.Node<String> current = root;
		//prints current node
		printTree(current);
	}

	//prints tree from node
	public void printTree(RedBlackTree.Node<String> node) {
		//prints key of node where printing starts from
		System.out.print(node.key);
		//if the node has no left and right child return nothing
		if (node.leaf()) {
			return;
		}
		//recursively prints children
		printTree(node.leftChild);
		printTree(node.rightChild);
	}

	// adds a new node to the tree and makes it red
	public void addNode(String data) { 
		//creates new node object which holds a string
		RedBlackTree.Node<String> newNode = new RedBlackTree.Node<String>(data);
		//makes it red
		newNode.red = true;
		newNode.color = 0;
		//if the root is empty then make the root the new node
		if (root == null) {
			root = newNode;
		} else {
			//sets current node to the root
			RedBlackTree.Node<String> current = root;
			//infinite loop until broken by break
			while (true) {
				//while the current node is greater than the new node
				if (current.compareTo(newNode) > 0) {
					//if left child is empty
					if (current.leftChild == null) {
						//sets left child to the new node
						current.leftChild = newNode;
						//sets the parent of the new Node to current
						newNode.parent = current;
						//breaks the loop
						break;
					}
					//now sets current to the new node
					current = current.leftChild;
				} 
				//if the current node is smaller than the new node
				else if (current.compareTo(newNode) < 0) {
					if (current.rightChild == null) {
						current.rightChild = newNode;
						newNode.parent = current;
						break;
					}
					current = current.rightChild;
				}
			}
		}
		//calls method fixTree to readjust the red black tree to maintain properties
		fixTree(newNode);
	}
	
	//inserts data into node
	public void insert(String data) {
		addNode(data);
	}

	//searches for a key
	public RedBlackTree.Node<String> lookUp(String key) {
		//if there is no tree then there is nothing to compare
		if (root == null) {
			return null;
		} 
		//if tree isn't empty
		else {
			//sets current node to root
			RedBlackTree.Node<String> current = root;
			//loop runs as long as the current node's key isn't equal to the key we're looking for
			while (!current.key.equals(key)) {
				//if the current node's key is greater than the one we're looking for
				if (current.key.compareTo(key) > 0) {
					//if left child of current node is empty
					if (current.leftChild == null) {
						//if the current node is bigger than the key we're looking for and there's nothing in the left node
						//that means that the key doesn't exist as everything on the left is smaller than the right
						return null;
					}
					//sets current to the left child of current
					current = current.leftChild;
				} else if (current.key.compareTo(key) <= 0) {
					if (current.rightChild == null) {
						return null;
					}
					current = current.rightChild;
				}
			}
			//returns current
			return current;
		}
	}

	//gets the sibling of a node
	//sibling of a left node is the right node of its parent
	//sibling of a right node is the left node of its parent
	public RedBlackTree.Node<String> getSibling(RedBlackTree.Node<String> node) {
		//returns nothing if the selected node is a left node and there exist no right node for its parent
		if (node == node.parent.leftChild) {
			if (node.parent.rightChild == null) {
				return null;
			}
			return node.parent.rightChild;
		} 
		//same as above but the selected node is a right node
		else {
			if (node.parent.leftChild == null) {
				return null;
			}
			return node.parent.leftChild;
		}
	}

	//returns the aunt of the node
	//aunt is the node's parent's sibling
	public RedBlackTree.Node<String> getAunt(RedBlackTree.Node<String> node) {
		//if the parent is the root then that means there can't be any siblings 
		if (node.parent == root) {
			return null;
		}
		//is the parent is the left child
		if (node.parent == getGrandparent(node).leftChild) {
			if (getGrandparent(node).rightChild == null) {
				return null;
			}
			//then the aunt is the right child
			return getGrandparent(node).rightChild;
		} else {
			if (getGrandparent(node).leftChild == null) {
				return null;
			}
			return getGrandparent(node).leftChild;
		}
	}
	
	//returns the grandparent of selected node
	public RedBlackTree.Node<String> getGrandparent(RedBlackTree.Node<String> node) {
		//returns nothing if node's parent has no parent
		if (node.parent.parent == null) {
			return null;
		}
		return node.parent.parent;
	}

	//rotates tree left around the parameter i
	public void rotateLeft(RedBlackTree.Node<String> i) {
		//sets node j as i's right child
		RedBlackTree.Node<String> j = i.rightChild;
		//sets the right child of i to j's left child
		i.rightChild = j.leftChild;
		//if j has a left child
		if (j.leftChild != null) {
			j.leftChild.parent = i;
		}
		j.parent = i.parent;
		//if i has no parent
		if (i.parent == null) {
			root = j;
		} 
		//if i is the left child
		else if (i == i.parent.leftChild) {
			i.parent.leftChild = j;
		} else {
			i.parent.rightChild = j;
		}
		j.leftChild = i;
		i.parent = j;
	}
	
	//same as rotateLeft but instead rotates right
	public void rotateRight(RedBlackTree.Node<String> j) {
		RedBlackTree.Node<String> i = j.leftChild;
		j.leftChild = i.rightChild;
		if (j.rightChild != null) {
			j.rightChild.parent = j;
		}
		i.parent = j.parent;
		if (j.parent == null) {
			root = i;
		} else if (j == j.parent.rightChild) {
			j.parent.rightChild = j;
		} else {
			j.parent.leftChild = j;
		}
		i.rightChild = j;
		j.parent = i;
	}
	
	//fixes the tree in order to maintain redBlackTree properties
	public void fixTree(RedBlackTree.Node<String> current) {
		//when the root is the current node
		if (current == root) {
			current.red = false;
			current.color = 1;
			return;
		}
		//if the parent is black do nothing
		if (current.parent.red == false) {
			return;
		}
		//if the parent is red and the current node is red
		if (current.red == true && current.parent.red == true) {
			//the aunt node is black or is empty
			if (getAunt(current) == null || getAunt(current).red == false) {
				//the parent of the current node is the left child of current's grandparent
				// Current is right child of parent
				if (current == current.parent.rightChild && current.parent == getGrandparent(current).leftChild) {
					//rotates tree left around current's parent
					rotateLeft(current.parent);
					//calls fixTree around the parent of current
					fixTree(current.parent);
				}
				// current's parent is the right child
				// current is the left child
				else if (current == current.parent.leftChild && current.parent == getGrandparent(current).rightChild) {
					rotateRight(current.parent);
					fixTree(current.parent);
				}
				// current's parent is the left child
				//current is the left child
				else if (current == current.parent.leftChild && current.parent == getGrandparent(current).leftChild) {
					//sets current's parent's color to black
					current.parent.red = false;
					current.parent.color = 1;
					//sets current's grandparent's color to red
					getGrandparent(current).red = true;
					getGrandparent(current).color = 0;
					rotateRight(getGrandparent(current));
					return;
				}
				//if current's parent is the right child
				// current is the right child
				else if (current == current.parent.rightChild && current.parent == getGrandparent(current).rightChild) {
					current.parent.red = false;
					current.parent.color = 1;
					getGrandparent(current).red = true;
					getGrandparent(current).color = 0;
					rotateLeft(getGrandparent(current));
					return;
				}
			}
			//the aunt of current is red
			else if (getAunt(current).red == true) {
				//sets parent's color to black
				current.parent.red = false;
				current.parent.color = 1;
				//sets aunt's color to black
				getAunt(current).red = false;
				getAunt(current).color = 1;
				//sets grandparent's color to red
				getGrandparent(current).red = true;
				getGrandparent(current).color = 0;
				//fixes tree around grandparent
				fixTree(getGrandparent(current));
			}
		}
	}
	
	//determines if a node is empty
	public boolean empty(RedBlackTree.Node<String> n) {
		if (n.key == null) {
			return true;
		}
		return false;
	}
	
	//determines if node is a left child of a parent
	public boolean isLeftChild(RedBlackTree.Node<String> parent, RedBlackTree.Node<String> child) {
		//if child is smaller than parent
		if (child.compareTo(parent) < 0) {
			return true;
		}
		return false;
	}

	
}
