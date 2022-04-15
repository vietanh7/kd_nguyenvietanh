package com.test.demo.data.db.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.demo.data.remote.model.Product


@Entity(tableName = ProductEntity.TABLE_NAME)
data class ProductEntity(
    @ColumnInfo(name = ID)
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = SKU)
    val sku: String,

    @ColumnInfo(name = IMAGE)
    val image: String?,

    @ColumnInfo(name = PRICE)
    val price: Long,

    @ColumnInfo(name = PRODUCT_NAME)
    val productName: String,

    @ColumnInfo(name = QUANTITY)
    val quantity: Int,

    @ColumnInfo(name = STATUS)
    val status: Int,

    @ColumnInfo(name = UNIT)
    val unit: String,

    @ColumnInfo(name = UPDATE_AT)
    val updateAt: Long = System.currentTimeMillis()
) {

    fun toProduct(): Product {
        return Product(
            createdAt = "",
            updatedAt = "",
            id = id,
            sku = sku,
            image = image,
            productName = productName,
            price = price,
            status = status,
            qty = quantity,
            unit = unit,
            message = "",
            success = true
        )
    }

    companion object Schema {
        const val TABLE_NAME = "product"

        const val SKU = "sku"
        const val ID = "id"
        const val IMAGE = "image"
        const val PRICE = "price"
        const val PRODUCT_NAME = "product_name"
        const val QUANTITY = "quantity"
        const val STATUS = "status"
        const val UNIT = "unit"
        const val UPDATE_AT = "update_at"

        fun from(product: Product): ProductEntity {
            return ProductEntity(
                id = product.id,
                sku = product.sku,
                image = product.image,
                productName = product.productName,
                price = product.price,
                status = product.status,
                quantity = product.qty,
                unit = product.unit
            )
        }
    }
}