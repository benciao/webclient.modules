package com.ecg.webclient.feature.administration.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.feature.administration.authentication.AuthenticationUtil;
import com.ecg.webclient.feature.administration.service.ClientService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.ClientConfig;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;
import com.ecg.webclient.feature.administration.viewmodell.validator.ClientDtoValidator;

/**
 * Controller zur Bearbeitung von Requests aus Administrationsdialogen (Mandant).
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/client")
public class ClientController
{
    static final Logger        logger = LogManager.getLogger(ClientController.class.getName());

    @Autowired
    private ClientService      clientService;
    @Autowired
    private AuthenticationUtil authUtil;
    @Autowired
    ClientDtoValidator         clientDtoValidator;

    /**
     * Behandelt POST-Requests vom Typ "/admin/client/save". Speichert Änderungen an Mandanten.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveClient(@Valid ClientConfig clientConfig, BindingResult bindingResult)
    {
        List<ClientDto> updateDtos = new ArrayList<ClientDto>();
        List<ClientDto> deleteDtos = new ArrayList<ClientDto>();

        for (ClientDto dto : clientConfig.getClients())
        {
            if (dto.isDelete())
            {
                deleteDtos.add(dto);
            }
            else
            {
                updateDtos.add(dto);
            }
        }

        clientService.deleteClients(deleteDtos);
        updateSelectedClient(deleteDtos);

        clientConfig.removeDeleted();

        if (bindingResult.hasErrors())
        {
            return getLoadingRedirectTemplate();
        }

        clientService.saveClients(updateDtos);
        updateSelectedClient(updateDtos);

        return "redirect:";
    }

    /**
     * Behandelt GET-Requests vom Typ "/admin/client". Lädt alle Mandanten.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(method = RequestMethod.GET)
    public String showClientConfig(Model model)
    {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClients(clientService.getAllClients(false));
        model.addAttribute("clientConfig", clientConfig);
        return getLoadingRedirectTemplate();
    }

    protected String getLoadingRedirectTemplate()
    {
        return "feature/administration/clientconfig";
    }

    @InitBinder("clientConfig")
    protected void initClientBinder(WebDataBinder binder)
    {
        binder.setValidator(clientDtoValidator);
    }

    private void updateSelectedClient(List<ClientDto> clientDtos)
    {
        for (ClientDto clientDto : clientDtos)
        {
            if (authUtil.getSelectedClient().getId() == clientDto.getId())
            {
                authUtil.setSelectedClientWithNewAuthority(clientDto);
                break;
            }
        }
    }
}
