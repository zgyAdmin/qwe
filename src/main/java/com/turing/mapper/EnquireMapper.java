package com.turing.mapper;

import com.github.pagehelper.PageInfo;
import com.turing.entity.Enquire;
import com.turing.entity.EnquireExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface EnquireMapper {
    long countByExample(EnquireExample example);

    int deleteByExample(EnquireExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Enquire record);

    int insertSelective(Enquire record);

    List<Enquire> selectByExample(EnquireExample example);

    Enquire name(Long id);

    int updateByExampleSelective(@Param("record") Enquire record, @Param("example") EnquireExample example);

    int updateByExample(@Param("record") Enquire record, @Param("example") EnquireExample example);

    int updateByPrimaryKeySelective(Enquire record);

    int updateByPrimaryKey(Enquire record);

	Enquire selectByPrimaryKey(long id);
	
	List<Map<String, Object>> selectEnquires(@Param("status")String status);
}