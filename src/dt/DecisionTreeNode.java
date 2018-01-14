package dt;

/** @author amoraitis */
public class DecisionTreeNode {
	private DecisionTreeNode left;
	private DecisionTreeNode right;
	private String tag;
	private int index, value;

	public DecisionTreeNode(String tag, int index) {
		setLeft(null);
		setRight(null);
		this.index = index;
		this.tag = tag;
		
	}

	public boolean isSLeaf() {
		return (left == null && right == null);
	}

	/** @return the left */
	public DecisionTreeNode getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(DecisionTreeNode left) {
		this.left = left;
	}

	/** @return the right */
	public DecisionTreeNode getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(DecisionTreeNode right) {
		this.right = right;
	}

	/** @return the tag */
	public String getTag() {
		return tag;
	}

	/** @return the index */
	public int getIndex() {
		return index;
	}

	/** @return the value */
	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	public void setTag(String tag) {
		this.tag = tag;

	}
}
