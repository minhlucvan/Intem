package intem.mudules.stampfactory.output;

import intem.mudules.stampfactory.factory.StampDocument;

/**
 * Created by minh on 27/09/16.
 */
public interface StampCollection {
    void save(String path);
    void addStamp(StampDocument stamp);
}
