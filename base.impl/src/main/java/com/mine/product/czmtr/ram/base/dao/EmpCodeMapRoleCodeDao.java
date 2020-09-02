package com.mine.product.czmtr.ram.base.dao;

import com.mine.product.czmtr.ram.base.model.EmpCodeMapRoleCodeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpCodeMapRoleCodeDao  extends JpaRepository<EmpCodeMapRoleCodeModel, String>  {
}
