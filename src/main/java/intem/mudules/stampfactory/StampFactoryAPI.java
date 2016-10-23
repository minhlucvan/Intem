package intem.mudules.stampfactory;

import intem.mudules.stampfactory.factory.StampData;
import intem.mudules.stampfactory.factory.StampDocument;
import intem.mudules.stampfactory.input.StampFactoryOption;

/**
 * Created by minh on 27/09/16.
 * <p>
 * Interface StampController
 *
 * @Input: - factory template
 * - template options
 * - factory options
 * - csv serial list
 * - output options
 * @output: - log info
 * - stamp in template pdf, image
 */
public interface StampFactoryAPI {
    void setOption(StampFactoryOption option);

    void setTemplate(StampDocument document);

    void setData(StampData data);

    void setOutPutOption();

    void render();

    void trigger(StampFactoryEvent e);

    void on(int event, StampFactoryActionListener handler);
}
