package eu.europa.ec.fisheries.uvms.rules.service.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.v1.ActionType;
import eu.europa.ec.fisheries.schema.rules.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesDataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.RulesParameterService;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.business.PositionFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.ServiceException;

@Stateless
public class RulesServiceBean implements RulesService {

    final static Logger LOG = LoggerFactory.getLogger(RulesServiceBean.class);

    @EJB
    RulesParameterService parameterService;

    @EJB
    RulesResponseConsumer consumer;

    @EJB
    RulesMessageProducer producer;

    /**
     * {@inheritDoc}
     *
     * @param customRule
     * @throws ServiceException
     */
    @Override
    public CustomRuleType createCustomRule(CustomRuleType customRule) throws ServiceException {
        LOG.info("Create invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapCreateCustomRule(customRule);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            return RulesDataSourceResponseMapper.mapToCreateCustomRuleFromResponse(response);
        } catch (ModelMapperException | MessageException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     * @throws ServiceException
     */
    @Override
    public List<CustomRuleType> getCustomRuleList() throws ServiceException {
        LOG.info("Get list invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapListCustomRule();
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            return RulesDataSourceResponseMapper.mapToCustomRuleListFromResponse(response);
        } catch (ModelMapperException | MessageException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    // Triggered by rule engine, no response expected
    @Override
    public void createErrorReport(String comment, String guid) throws ServiceException {
        LOG.info("Get list invoked in service layer");
        try {
            String request = RulesDataSourceRequestMapper.mapCreateErrorReport(comment, guid);
            producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
        } catch (ModelMapperException | MessageException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    // Triggered by rule engine, no response expected
    @Override
    public void customRuleTriggered(PositionFact p, String actions) {
        LOG.info("Creating custom event. NOT IMPLEMENTED");

        // For now the actions are described as a comma separated list. Parse
        // out the action, switch on it, and log the action and the
        // corresponding value
        // ACTION,VALUE;ACTION,VALUE;
        // N.B! The .drl rule file gives the string "null" when for instance
        // value is null.
        String[] parsedActionKeyValueList = actions.split(";");
        for (String keyValue : parsedActionKeyValueList) {
            String[] keyValueList = keyValue.split(",");
            String action = keyValueList[0];
            String value = "";
            if (keyValueList.length == 2) {
                value = keyValueList[1];
            }
            switch (ActionType.valueOf(action)) {
            case EMAIL:
                LOG.info("Performing action '{}' with value '{}' regarding guid '{}'", action, value, p.getGuid());
                break;
            case ON_HOLD:
                LOG.info("Performing action '{}' with value '{}' regarding guid '{}'", action, value, p.getGuid());
                break;
            case OPEN_TICKET:
                LOG.info("Performing action '{}' with value '{}' regarding guid '{}'", action, value, p.getGuid());
                break;
            case MANUAL_POLL:
                LOG.info("Performing action '{}' with value '{}' regarding guid '{}'", action, value, p.getGuid());
                break;
            case SEND_TO_ENDPOINT:
                LOG.info("Performing action '{}' with value '{}' regarding guid '{}'", action, value, p.getGuid());
                break;
            case SMS:
                LOG.info("Performing action '{}' with value '{}' regarding guid '{}'", action, value, p.getGuid());
                break;
            case TOP_BAR_NOTIFICATION:
                break;
            default:
                // Unreachable, ActionType.valueOf(action) would fail before
                LOG.info("The action '{}' is not defined", action);
                break;
            }
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    @Override
    public CustomRuleType getById(Long id) throws ServiceException {
        LOG.info("Get by id invoked in service layer");
        throw new ServiceException("Get by id not implemented in service layer");
    }

    /**
     * {@inheritDoc}
     *
     * @param data
     * @throws ServiceException
     */
    @Override
    public CustomRuleType update(CustomRuleType customRuleType) throws ServiceException {
        LOG.info("Update invoked in service layer");
        throw new ServiceException("Update not implemented in service layer");
    }

}
