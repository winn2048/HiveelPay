package com.hiveelpay.common.model;

import java.io.Serializable;

public class HiveelPage implements Serializable {
    private int totalPages;
    private int perPageSize = 30;//default 30
    private int total;
    private int currentPage = 1;

    private int offset;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPerPageSize() {
        return perPageSize;
    }

    public void setPerPageSize(int perPageSize) {
        this.perPageSize = perPageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        if (total > 0) {
            if (this.total % this.perPageSize == 0) {
                this.totalPages = this.total / this.perPageSize;
            } else {
                this.totalPages = (this.total / this.perPageSize) + 1;
            }
            this.offset = (this.currentPage - 1) * this.perPageSize;
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


}
