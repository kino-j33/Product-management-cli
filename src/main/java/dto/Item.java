package dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO　商品が持っておくべき情報のテンプレ
 * DBと送受信するデータをこのテンプレに入れる
 * @author member
 */

public class Item {
	private Integer sid;
	private String id;
	private String code;
	private String name;
	private String category;
	private Integer salePrice;
	private Integer purchasePrice;
	private LocalDate registrationDate;
	private String deleted;
	private Integer versionNo;
	private LocalDateTime recordCreationTimestamp;
	private LocalDateTime recordUpdateTimestamp;

	/**
	 * 先に型だけ作る用コンストラクタ
	 * @author member
	 */
	public Item() {
	}

	/**
	 * 全部入りコンストラクタ
	 * @author member
	 */
	public Item(Integer sid,
			String id,
			String code,
			String name,
			String category,
			Integer salePrice,
			Integer purchasePrice,
			LocalDate registrationDate,
			String deleted,
			Integer versionNo,
			LocalDateTime recordCreationTimestamp,
			LocalDateTime recordUpdateTimestamp) {
		this.setSid(sid);
		this.setId(id);
		this.setCode(code);
		this.setName(name);
		this.setCategory(category);
		this.setSalePrice(salePrice);
		this.setPurchasePrice(purchasePrice);
		this.setRegistrationDate(registrationDate);
		this.setDeleted(deleted);
		this.setVersionNo(versionNo);
		this.setRecordCreationTimestamp(recordCreationTimestamp);
		this.setRecordUpdateTimestamp(recordUpdateTimestamp);
	}

	/**
	 * @author member
	 */
	public Integer getSid() {
		return sid;
	}

	/**
	 * @author member
	 */
	public void setSid(Integer sid) {
		this.sid = sid;
	}

	/**
	 * @author member
	 */
	public String getId() {
		return id;
	}

	/**
	 * @author member
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @author member
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @author member
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @author member
	 */
	public String getName() {
		return name;
	}

	/**
	 * @author member
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @author member
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @author member
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @author member
	 */
	public Integer getSalePrice() {
		return salePrice;
	}

	/**
	 * @author member
	 */
	public void setSalePrice(Integer salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * @author member
	 */
	public Integer getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @author member
	 */
	public void setPurchasePrice(Integer purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * @author member
	 */
	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @author member
	 */
	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @author member
	 */
	public String getDeleted() {
		return deleted;
	}

	/**
	 * @author member
	 */
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	/**
	 * @author member
	 */
	public Integer getVersionNo() {
		return versionNo;
	}

	/**
	 * @author member
	 */
	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	/**
	 * @author member
	 */
	public LocalDateTime getRecordCreationTimestamp() {
		return recordCreationTimestamp;
	}

	/**
	 * @author member
	 */
	public void setRecordCreationTimestamp(LocalDateTime recordCreationTimestamp) {
		this.recordCreationTimestamp = recordCreationTimestamp;
	}

	/**
	 * @author member
	 */
	public LocalDateTime getRecordUpdateTimestamp() {
		return recordUpdateTimestamp;
	}

	/**
	 * @author member
	 */
	public void setRecordUpdateTimestamp(LocalDateTime recordUpdateTimestamp) {
		this.recordUpdateTimestamp = recordUpdateTimestamp;
	}

	//	/**
	//	 * @author member
	//	 */
	//	@Override
	//	public String toString() {
	//		StringBuilder sb = new StringBuilder();
	//		sb.append("Item{")
	//				.append("sid=").append(sid)
	//				.append(", id='").append(id).append('\'')
	//				.append(", code='").append(code).append('\'')
	//				.append(", name='").append(name).append('\'')
	//				.append(", category='").append(category).append('\'')
	//				.append(", salePrice=").append(salePrice)
	//				.append(", purchasePrice=").append(purchasePrice)
	//				.append(", registrationDate=").append(registrationDate)
	//				.append(", deleted='").append(deleted).append('\'')
	//				.append(", versionNo=").append(versionNo)
	//				.append(", recordCreationTimestamp=").append(recordCreationTimestamp)
	//				.append(", recordUpdateTimestamp=").append(recordUpdateTimestamp)
	//				.append('}');
	//		return sb.toString();
	//	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(id).append(", ")
				.append(code).append(", ")
				.append(name).append(", ")
				.append(category).append(", ")
				.append(salePrice).append(", ")
				.append(purchasePrice).append(", ")
				.append(registrationDate);
		return sb.toString();
	}

}
