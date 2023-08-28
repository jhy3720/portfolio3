package com.project.code.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import com.project.code.entity.UserData

@Repository
interface  UserDataRepository : JpaRepository<UserData, Long> {


}