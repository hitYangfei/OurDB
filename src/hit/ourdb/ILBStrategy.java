

package hit.ourdb;
import java.util.List;

public interface ILBStrategy {
  public DataSource getOneDataSource(List<DataSource> sources);
}
