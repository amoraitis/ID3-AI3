/**
 * 
 */

/**
 * @author tasos
 *
 */
public class Abalone {
	private String sex;
	private double length, diameter,height, wholeWeight,shuckedWeight, visceraWeight, shellWeight;
	private int ring;
	public Abalone(){
		
	}
	
	/**
	 * @param sex
	 * @param length
	 * @param diameter
	 * @param height
	 * @param wholeWeight
	 * @param shuckedWeight
	 * @param visceraWeight
	 * @param shellWeight
	 * @param ring
	 */
	public Abalone(String sex, double length, double diameter, double height, double wholeWeight, double shuckedWeight,
			double visceraWeight, double shellWeight, int ring) {
		this.sex = sex;
		this.length = length;
		this.diameter = diameter;
		this.height = height;
		this.wholeWeight = wholeWeight;
		this.shuckedWeight = shuckedWeight;
		this.visceraWeight = visceraWeight;
		this.shellWeight = shellWeight;
		this.ring = ring;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the length
	 */
	public double getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(double length) {
		this.length = length;
	}
	/**
	 * @return the diameter
	 */
	public double getDiameter() {
		return diameter;
	}
	/**
	 * @param diameter the diameter to set
	 */
	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}
	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	/**
	 * @return the wholeWeight
	 */
	public double getWholeWeight() {
		return wholeWeight;
	}
	/**
	 * @param wholeWeight the wholeWeight to set
	 */
	public void setWholeWeight(double wholeWeight) {
		this.wholeWeight = wholeWeight;
	}
	/**
	 * @return the shuckedWeight
	 */
	public double getShuckedWeight() {
		return shuckedWeight;
	}
	/**
	 * @param shuckedWeight the shuckedWeight to set
	 */
	public void setShuckedWeight(double shuckedWeight) {
		this.shuckedWeight = shuckedWeight;
	}
	/**
	 * @return the visceraWeight
	 */
	public double getVisceraWeight() {
		return visceraWeight;
	}
	/**
	 * @param visceraWeight the visceraWeight to set
	 */
	public void setVisceraWeight(double visceraWeight) {
		this.visceraWeight = visceraWeight;
	}
	/**
	 * @return the shellWeight
	 */
	public double getShellWeight() {
		return shellWeight;
	}
	/**
	 * @param shellWeight the shellWeight to set
	 */
	public void setShellWeight(double shellWeight) {
		this.shellWeight = shellWeight;
	}
	/**
	 * @return the ring
	 */
	public int getRing() {
		return ring;
	}
	/**
	 * @param ring the ring to set
	 */
	public void setRing(int ring) {
		this.ring = ring;
	}
	@Override
	public String toString() {
		return this.getSex()+","+
				this.getLength()+","+
				this.getDiameter()+","+
				this.getHeight()+","+
				this.getWholeWeight()+","+
				this.getShuckedWeight()+","+
				this.getVisceraWeight()+","+
				this.getShellWeight()+","+
				this.getRing();
	}
}
