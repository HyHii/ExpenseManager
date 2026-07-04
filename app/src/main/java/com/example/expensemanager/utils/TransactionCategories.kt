package com.example.expensemanager.utils

data class CategoryGroup(
    val name: String,
    val subCategories: List<String>
)

val incomeCategoryGroups = listOf(
    CategoryGroup(
        name = "Lương & Trợ cấp (Salary & Allowance)**",
        subCategories = listOf(
            "Lương chính (Salary)",
            "Tiền làm thêm giờ (Overtime)",
            "Phụ cấp / Trợ cấp (Allowance)"
        )
    ),
    CategoryGroup(
        name = "Thưởng & Hoa hồng (Bonus & Commission)",
        subCategories = listOf(
            "Thưởng tháng / Quý / Năm (Bonus)",
            "Hoa hồng doanh thu (Commission)"
        )
    ),
    CategoryGroup(
        name = "Làm ngoài & Kinh doanh (Side Hustle & Business)**",
        subCategories = listOf(
            "Tiền dự án / Freelance (Freelance/Gig)",
            "Lợi nhuận kinh doanh (Business Income)",
            "Bán đồ cũ / Thanh lý (Selling items)"
        )
    ),
    CategoryGroup(
        name = "Đầu tư & Tài sản (Investment & Assets)",
        subCategories = listOf(
            "Lãi ngân hàng / Lãi tiết kiệm (Interest)",
            "Cổ tức / Crypto / Stocks (Dividends / Crypto / Stocks)",
            "Tiền cho thuê (Rental Income)"
        )
    ),
    CategoryGroup(
        name = "Nguồn thu khác (Others)",
        subCategories = listOf(
            "Được cho / Tặng / Lì xì (Gifts)",
            "Tiền hoàn trả (Refunds)",
            "Thu nhập khác (Other Income)"
        )
    )
)

val expenseCategoryGroups = listOf(
    CategoryGroup(
        name = "Ăn uống",
        subCategories = listOf(
            "Ăn sáng",
            "Ăn trưa",
            "Ăn tối",
            "Cà phê / Trà sữa",
            "Đồ ăn vặt",
            "Đặt đồ ăn"
        )
    ),
    CategoryGroup(
        name = "Di chuyển",
        subCategories = listOf(
            "Xăng xe",
            "Gửi xe",
            "Grab / Taxi",
            "Vé xe buýt",
            "Bảo dưỡng xe"
        )
    ),
    CategoryGroup(
        name = "Mua sắm",
        subCategories = listOf(
            "Quần áo",
            "Mỹ phẩm",
            "Đồ công nghệ",
            "Đồ dùng cá nhân",
            "Đồ gia dụng"
        )
    ),
    CategoryGroup(
        name = "Hóa đơn & Sinh hoạt",
        subCategories = listOf(
            "Tiền điện",
            "Tiền nước",
            "Internet",
            "Điện thoại",
            "Tiền thuê nhà"
        )
    ),
    CategoryGroup(
        name = "Giải trí",
        subCategories = listOf(
            "Xem phim",
            "Game",
            "Du lịch",
            "Đi chơi",
            "Subscription"
        )
    ),
    CategoryGroup(
        name = "Sức khỏe & Giáo dục",
        subCategories = listOf(
            "Thuốc men",
            "Khám bệnh",
            "Học phí",
            "Sách / Tài liệu",
            "Khóa học"
        )
    ),
    CategoryGroup(
        name = "Khác",
        subCategories = listOf(
            "Quà tặng",
            "Từ thiện",
            "Phát sinh khác"
        )
    )
)