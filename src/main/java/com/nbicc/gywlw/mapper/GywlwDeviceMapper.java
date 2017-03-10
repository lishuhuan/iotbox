package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GywlwDeviceMapper {
    int deleteByPrimaryKey(String deviceId);

    int insert(GywlwDevice record);

    int insertSelective(GywlwDevice record);

    List<GywlwDevice> selectByUserIdAndDeviceSn(@Param("userId")String userId,
                                               @Param("deviceSn")String deviceSn);
    List<GywlwDevice> selectByUserIdAndDeviceSnWithAdmin(@Param("userId")String userId,
                                                   @Param("deviceSn")String deviceSn);
    GywlwDevice selectByDeviceSn(String deviceSn);

    List<GywlwDevice> selectByAdminId(String adminId);

    List<GywlwDevice> selectByFactoryId(String factoryId);

    GywlwDevice selectByDeviceId(String deviceId);

    List<GywlwDevice> selectAll();

    int updateByDeviceSnSelective(GywlwDevice gywlwDevice);

    int updateByPrimaryKeySelective(GywlwDevice gywlwDevice);

    int updateByPrimaryKey(GywlwDevice record);

    int updateByaddIdAndQuitId(@Param("addId") String addId,
                               @Param("quitId")String quitId,
                               @Param("mark")String mark);
    
    public List<GywlwDevice> searchDeviceByFactory(String adminId);
    
    public List<GywlwDevice> getFactoryDevicelist(@Param("factoryId") String factoryId, @Param("deviceSn") String deviceSn,@Param("level") int level);

	GywlwDevice getDeviceAndTotal(@Param("deviceId") String deviceId);

	List<GywlwDevice> selectTotalAndDeviceByAdminId(@Param("userId") String userId);

	String getDeviceIdByGpioId(@Param("gpioId") String gpioId);
    
}