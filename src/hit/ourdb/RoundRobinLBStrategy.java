package hit.ourdb;
import java.util.List;
public class RoundRobinLBStrategy extends AbstractLBStrategy {
  private int count ;
  public RoundRobinLBStrategy()
  {
    count = 0;
  }
  public DataSource getOneDataSource(List<DataSource> sources)
  {
    int size = sources.size();
    count++;
    count = count%size;
    return sources.get(count);
  }
}
