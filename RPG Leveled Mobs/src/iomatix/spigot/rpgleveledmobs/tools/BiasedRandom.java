package iomatix.spigot.rpgleveledmobs.tools;

public class BiasedRandom {
	public static int randomIntAlter(int min,int max){
	    return (int) Math.floor(Math.abs(Math.random()-Math.random())*(1+max-min)+min);
	}
	
	public static double randomDoubleAlter(double min,double max){
	    return Math.abs(Math.random()-Math.random())*(1+max-min)+min;
	}
	
	public static int randomInt(int min,int max){
		 return (int) Math.floor((1 - Math.sqrt(1 - Math.random()))*((max-min)+min));
	}

	public static double randomDouble(double min,double max){
	    return (1 - Math.sqrt(1 - Math.random()))*((max-min)+min);
	}
}
