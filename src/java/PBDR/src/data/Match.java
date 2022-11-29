package data;

import java.io.Serializable;

public class Match implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String equipeD;
	private String equipeE;
	private int scoreD;
	private int scoreE;
	private Boolean joue;
	private int journee;
	private long equipeD_id;
	private long equipeE_id;

	public Match() {
		
	}
	
	public Match(long id, String equipeD, String equipeE, int scoreD, int scoreE, Boolean joue, int journee,
			long equipeD_id, long equipeE_id) {
		this.id = id;
		this.equipeD = equipeD;
		this.equipeE = equipeE;
		this.scoreD = scoreD;
		this.scoreE = scoreE;
		this.joue = joue;
		this.journee = journee;
		this.equipeD_id = equipeD_id;
		this.equipeE_id = equipeE_id;
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEquipeD() {
		return equipeD;
	}

	public void setEquipeD(String equipeD) {
		this.equipeD = equipeD;
	}

	public String getEquipeE() {
		return equipeE;
	}

	public void setEquipeE(String equipeE) {
		this.equipeE = equipeE;
	}

	public int getScoreD() {
		return scoreD;
	}

	public void setScoreD(int scoreD) {
		this.scoreD = scoreD;
	}

	public int getScoreE() {
		return scoreE;
	}

	public void setScoreE(int scoreE) {
		this.scoreE = scoreE;
	}

	public Boolean isJoue() {
		return joue;
	}

	public void setJoue(Boolean joue) {
		this.joue = joue;
	}

	public int getJournee() {
		return journee;
	}

	public void setJournee(int journee) {
		this.journee = journee;
	}

	public long getEquipeD_id() {
		return equipeD_id;
	}

	public void setEquipeD_id(long equipeD_id) {
		this.equipeD_id = equipeD_id;
	}

	public long getEquipeE_id() {
		return equipeE_id;
	}

	public void setEquipeE_id(long equipeE_id) {
		this.equipeE_id = equipeE_id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
