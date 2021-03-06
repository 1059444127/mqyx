package com.psc.pt.dao.user;

import com.psc.pt.model.user.UserGroup;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
@Component("UserGroupMapper")
public interface UserGroupMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     * @param id  Primary key for table user_group
     * @return deleteByPrimaryKey
     *
     * @mbggenerated
     */
    @Delete({
        "delete from user_group",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     * @param record  Model for table user_group
     * @return   The last insert key for table user_group
     *
     * @mbggenerated
     */
    @Insert({
        "insert into user_group (group_name, features, ",
        "main_src)",
        "values (#{groupName,jdbcType=VARCHAR}, #{features,jdbcType=VARCHAR}, ",
        "#{mainSrc,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT SCOPE_IDENTITY()", keyProperty="id", before=false, resultType=Long.class)
    int insert(UserGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     * @param record  Model for table user_group
     * @return   The last insert key for table user_group
     *
     * @mbggenerated
     */
    @InsertProvider(type=UserGroupSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT SCOPE_IDENTITY()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(UserGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     * @param id  Primary key for table user_group
     * @return   Model for table user_group
     *
     * @mbggenerated
     */
    @Select({
        "select",
        "id, group_name, features, main_src",
        "from user_group",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="group_name", property="groupName", jdbcType=JdbcType.VARCHAR),
        @Result(column="features", property="features", jdbcType=JdbcType.VARCHAR),
        @Result(column="main_src", property="mainSrc", jdbcType=JdbcType.VARCHAR)
    })
    UserGroup selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     * @param record  Model for table user_group
     * @return   The last insert key for table user_group
     *
     * @mbggenerated
     */
    @UpdateProvider(type=UserGroupSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     * @param record  Model for table user_group
     * @return   The last insert key for table user_group
     *
     * @mbggenerated
     */
    @Update({
        "update user_group",
        "set group_name = #{groupName,jdbcType=VARCHAR},",
          "features = #{features,jdbcType=VARCHAR},",
          "main_src = #{mainSrc,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserGroup record);
}