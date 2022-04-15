package com.test.demo.data.db.product

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
abstract class ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveProducts(products: List<ProductEntity>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveProduct(product: ProductEntity): Completable

    @Query("SELECT * FROM ${ProductEntity.TABLE_NAME}")
    abstract fun getAllProduct(): Single<List<ProductEntity>>

    @Query("DELETE FROM ${ProductEntity.TABLE_NAME}")
    abstract fun clearProduct(): Completable

    @Query("DELETE FROM ${ProductEntity.TABLE_NAME} WHERE ${ProductEntity.ID} = :id")
    abstract fun deleteItem(id: Int): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateProduct(product: ProductEntity): Completable
}