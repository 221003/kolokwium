package edu.iis.mto.oven;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OvenTest {

	@Mock
	private HeatingModule heatingModule;
	
	@Mock
	private Fan fan;
	
	private Oven oven;
	
	@BeforeEach
	void setUp() {
		oven = new Oven(heatingModule, fan);
	}
	
    @Test
    void itCompiles() {
        MatcherAssert.assertThat(true, equalTo(true));
    }

    
    @Test
    void startShouldThrowHeatingException() throws OvenException, HeatingException {
    	
    	ProgramStage programStage = ProgramStage.builder()
    			.withHeat(HeatType.THERMO_CIRCULATION)
    			.withTargetTemp(1)
    			.withStageTime(10)
    			.build(); 
    	
    	HeatingSettings heatingSettings = HeatingSettings.builder()
    			.withTargetTemp(1)
    			.withTimeInMinutes(10)
    			.build();
    	
    	List<ProgramStage> programStages=  List.of(programStage);
    	
    	BakingProgram bakingProgram = BakingProgram.builder().withStages(programStages).build();
    	
//    	when(heatingModule.termalCircuit(heatingSettings);).thenThrow(HeatingException.class);
    	doThrow(HeatingException.class).when(heatingModule).termalCircuit(heatingSettings);
    	
    	
    	assertThrows(OvenException.class,()->oven.start(bakingProgram));
    
    }
    
    @Test
    void grilMethodShouldBeCalledOnce() throws HeatingException {
    	ProgramStage programStage = ProgramStage.builder()
    			.withHeat(HeatType.GRILL)
    			.withTargetTemp(1)
    			.withStageTime(10)
    			.build(); 
    	
    	HeatingSettings heatingSettings = HeatingSettings.builder()
    			.withTargetTemp(1)
    			.withTimeInMinutes(10)
    			.build();
    	
    	List<ProgramStage> programStages=  List.of(programStage);
    	
    	BakingProgram bakingProgram = BakingProgram.builder().withStages(programStages).build();
    	oven.start(bakingProgram);
    	verify(heatingModule, times(1)).grill(heatingSettings);
    }
    
    @Test
    void heaterMethodShouldBeCalledOnce() {
    	ProgramStage programStage = ProgramStage.builder()
    			.withHeat(HeatType.HEATER)
    			.withTargetTemp(1)
    			.withStageTime(10)
    			.build(); 
    	
    	HeatingSettings heatingSettings = HeatingSettings.builder()
    			.withTargetTemp(1)
    			.withTimeInMinutes(0)
    			.build();
    	
    	List<ProgramStage> programStages=  List.of(programStage);
    	
    	BakingProgram bakingProgram = BakingProgram.builder().withInitialTemp(1).build();
    	oven.start(bakingProgram);
    	

    	verify(heatingModule, times(1)).heater(heatingSettings);
    }
    
    
}
