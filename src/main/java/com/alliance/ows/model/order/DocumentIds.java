package com.alliance.ows.model.order;

public class DocumentIds {
	private CustomerDocumentId customerDocumentId;
	private DocumentId documentId;
	public SupplierDocumentId getSupplierDocumentId() {
		return supplierDocumentId;
	}

	public void setSupplierDocumentId(SupplierDocumentId supplierDocumentId) {
		this.supplierDocumentId = supplierDocumentId;
	}

	private SupplierDocumentId supplierDocumentId;
	public DocumentId getDocumentId() {
		return documentId;
	}

	public void setDocumentId(DocumentId documentId) {
		this.documentId = documentId;
	}

	public CustomerDocumentId getCustomerDocumentId() {
		return customerDocumentId;
	}

	public void setCustomerDocumentId(CustomerDocumentId customerDocumentId) {
		this.customerDocumentId = customerDocumentId;
	}
}
