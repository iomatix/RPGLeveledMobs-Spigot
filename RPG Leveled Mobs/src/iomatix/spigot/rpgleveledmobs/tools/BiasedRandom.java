package iomatix.spigot.rpgleveledmobs.tools;

public class BiasedRandom {
	//old formula
	public static int randomIntOld(int min,int max){
	    return (int) Math.floor(Math.abs(Math.random()-Math.random())*(1+max-min)+min);
	}
	
	public static double randomDoubleOld(double min,double max){
	    return Math.abs(Math.random()-Math.random())*(1+max-min)+min;
	}
	
	//PoW formula
	public static int randomIntAlter(int min,int max){
		 return (int) Math.floor( (Math.random() * (1+Math.random() * ((max-min)+min)) ));
	}

	public static double randomDoubleAlter(double min,double max){
	    return  Math.random() * (1+Math.random() * ((max-min)+min));
	}
	
	//sqrt formula
	public static int randomInt(int min,int max){
		 return (int) Math.floor((1 - Math.sqrt(1 - Math.random()))*((1+max-min)+min));
	}

	public static double randomDouble(double min,double max){
	    return (1 - Math.sqrt(1 - Math.random()))*((1+max-min)+min);
	}
}