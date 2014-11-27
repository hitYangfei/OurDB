package hit.ourdb;

public abstract class AbstractLBStrategy implements ILBStrategy {
  public static ILBStrategy strategyFactory(LBStrategyType type)
  {
    switch(type) {
      case RoundRobinStrategy:
        return new RoundRobinLBStrategy();
      default:
        return null;
    }
  }
}
