package vmgo.domain.interfaces;

import vmgo.domain.dto.ConstellationJoinDto;

public interface UserConstellationInterface {
	String getStatus();
	String getConstellationId();
	Integer getConLevel();
	Integer getConStep();
	String getConTitle();
	String getConDescription();
	String getConCompName();
	String getImagePath();
	String getUserConId();
	Integer getTotalStep();
	
	public default ConstellationJoinDto.DBData toDto() {
		return new ConstellationJoinDto.DBData(getStatus(), getConstellationId(), getConLevel()
				, getConStep(), getConTitle(), getConDescription(), getConCompName()
				, getImagePath(), getUserConId(), getTotalStep());
	}
}
