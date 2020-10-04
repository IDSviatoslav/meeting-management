import React, { useState, useEffect } from "react";
import DateTimePicker from "react-datetime-picker";

function MeetingEditor(props) {
  const url = "http://127.0.0.1:8080";

  var methodType = "POST";

  var selectedDepId;
  var selectedOrgId;

  const [meetingTime, setMeetingTime] = useState([new Date()]);
  const [meetingParticipants, setMeetingParticipants] = useState([]);
  const [meetingDepartmentId, setMeetingDepartmentId] = useState();
  const [meetingOrganizerId, setMeetingOrganizerId] = useState();
  const [meetingTheme, setMeetingTheme] = useState();

  const [receivedEmployees, setReceivedEmployees] = useState([]);
  const [receivedDepartments, setReceivedDepartments] = useState([]);
  const [receivedDepartmentMembers, setReceivedDepartmentMembers] = useState(
    []
  );

  useEffect(() => {
    console.log("id: " + props.meetingId + " workMode: " + props.workMode);
    getDepartmentsQuery();
    getEmployeesQuery();
    if (props.workMode === "CREATE") {
    } else if (props.workMode === "EDIT") {
      if (props.meetingId != null) {
        console.log("get one meeting");
        getMeetingQuery(props.meetingId);
      }
    }
  }, [props.workMode]);

  const getEmployeesQuery = (event) => {
    console.log("in empl querry");
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(url + "/employees", requestOptions)
      .then((response) => response.json())
      .then((response) => {
        setReceivedEmployees(response);
        console.log("receivedEmpl: ");
        console.log(response);
      })
      .catch((err) => console.log("Error " + err));
  };

  const getDepartmentsQuery = (event) => {
    console.log("in dep querry");
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(url + "/departments", requestOptions)
      .then((response) => response.json())
      .then((response) => {
        console.log(response);
        setReceivedDepartments(response);
      })
      .catch((err) => console.log("Error " + err));
  };

  const getMeetingQuery = (id) => {
    console.log("in meet querry id querry");
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(url + "/meeting" + "/" + id, requestOptions)
      .then((response) => response.json())
      .then((response) => {
        console.log("MEETING:");
        console.log(response);
        setMeetingTheme(response.theme);
        setMeetingTime(convertStringToDate(response.time));
        setMeetingParticipants(response.participants);
        setMeetingDepartmentId(response.department.id);
        setSelectValueById("dep-select", response.department.name);
        setReceivedDepartmentMembers(response.department.members);

        // var organizer = response.department.members.find((employee) => {
        //   console.log(
        //     "in find empId: " + employee.id + " orgId: " + response.organizerId
        //   );
        //   return employee.id == response.organizerId;
        // });

        // console.log("organizer: " + organizer);

        // console.log("organizer id: " + response.organizerId);
        // setSelectValueById(
        //   "org-select",
        //   response.department.members.find((employee) => {
        //     return employee.id == response.organizerId;
        //   }).name
        // );
        // setMeetingOrganizerId(response.organizerId);
        setSelectValueById("org-select", response.organizer.name);
      })
      .catch((err) => console.log("Error " + err));
  };

  const createMeetingQuery = (event) => {
    event.preventDefault();

    // selectedDepId = getIdFromSelect("dep-select");
    // selectedOrgId = getIdFromSelect("org-select");
    var meetParticipantIds = [];
    console.log(
      "theme = " +
        meetingTheme +
        " depId" +
        selectedDepId +
        " orgId" +
        selectedOrgId +
        " timetype = " +
        typeof meetingTime
    );

    meetingParticipants.map((employee) => {
      meetParticipantIds.push(employee.id);
    });

    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        theme: meetingTheme,
        time: formatDate(meetingTime),
        departmentId: meetingDepartmentId,
        organizerId: meetingOrganizerId,
        participantIds: meetParticipantIds,
      }),
    };
    //console.log("body: " + requestOptions.body);

    fetch(url + "/meeting", requestOptions)
      .then((response) => response.json())
      .catch((err) => console.log("Error: " + err));
  };

  function deleteParticipant(employee) {
    setReceivedEmployees([...receivedEmployees, employee]);
    var index = meetingParticipants.indexOf(employee);
    var tempArr = meetingParticipants;
    if (index !== -1) {
      tempArr.splice(index, 1);
      setMeetingParticipants(tempArr);
    }
  }

  function getIdFromSelect(selectId) {
    let select = document.getElementById(selectId);
    const selectedIndex = select.selectedIndex;
    return select.options[selectedIndex].getAttribute("id-key");
  }

  function setSelectValueById(id, value) {
    let select = document.getElementById(id);
    //select.selectedIndex =
    select.value = value;
  }

  function addEmployeeToMeeting(event) {
    event.preventDefault();
    if (receivedEmployees.length) {
      let employeeId = getIdFromSelect("part-select");

      var entry = receivedEmployees.find((employee) => {
        return employee.id == employeeId;
      });
      setMeetingParticipants([...meetingParticipants, entry]);
      var index = receivedEmployees.indexOf(entry);
      var tempArr = receivedEmployees;
      if (index !== -1) {
        tempArr.splice(index, 1);
        setReceivedEmployees(tempArr);
      }
    }
  }

  function selectDepartment(event) {
    var selectedIndex = event.target.options.selectedIndex;
    setReceivedDepartmentMembers(
      receivedDepartments[Object.keys(receivedDepartments)[selectedIndex - 1]]
        .members
    );
    setMeetingDepartmentId(
      event.target.options[selectedIndex].getAttribute("id-key")
    );
  }

  function selectOrganizer(event) {
    var selectedIndex = event.target.options.selectedIndex;
    var organizerId = event.target.options[selectedIndex].getAttribute(
      "id-key"
    );
    setMeetingOrganizerId(organizerId);

    var entry = receivedEmployees.find((employee) => {
      return employee.id == organizerId;
    });
    var index = receivedEmployees.indexOf(entry);
    var tempArr = receivedEmployees;
    if (index !== -1) {
      tempArr.splice(index, 1);
      setReceivedEmployees(tempArr);
    }
  }

  function changeTheme(event) {
    setMeetingTheme(event.target.value);
  }

  function formatDate(date) {
    var d = new Date(date),
      month = "" + (d.getMonth() + 1),
      day = "" + d.getDate(),
      year = d.getFullYear(),
      hour = "" + d.getHours(),
      minute = "" + d.getMinutes();

    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;
    if (hour.length < 2) hour = "0" + hour;
    if (minute.length < 2) minute = "0" + minute;

    return [year, month, day].join("-") + " " + [hour, minute].join(":");
  }

  function convertStringToDate(dateString) {
    var dateAndTime = dateString.split(" ");
    var date = dateAndTime[0].split("-");
    var time = dateAndTime[1].split(":");
    var mydate = new Date(date[0], date[1] - 1, date[2], time[0], time[1]);
    return mydate;
  }

  return (
    <form class="flexbox-vertical">
      <label class="flexbox-horizontal">
        <l1 class="bold-text">Тема</l1>
        <input type="text" onChange={changeTheme} value={meetingTheme} />
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Время проведения</l1>
        <div>
          <DateTimePicker
            id="time-form"
            value={meetingTime}
            onChange={setMeetingTime}
          />
        </div>
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Подразделение</l1>
        <select id="dep-select" onChange={selectDepartment}>
          <option value="none" selected disabled hidden>
            Подразделение
          </option>
          {receivedDepartments.map((department) => {
            return <option id-key={department.id}> {department.name}</option>;
          })}
        </select>
      </label>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Ответственный</l1>
        <select id="org-select" onChange={selectOrganizer}>
          <option value="none" selected disabled hidden>
            Работник
          </option>
          {receivedDepartmentMembers.map((member) => (
            <option id-key={member.id}>{member.name}</option>
          ))}
        </select>
      </label>

      <div>
        <l1 id="title">Участники</l1>
        <table id="participants">
          <tbody>
            {meetingParticipants.map((employee) => {
              return (
                <tr key={employee.id}>
                  <td>{employee.name}</td>
                  <td>{employee.age}</td>
                  <td>{employee.department}</td>
                  <td onClick={() => deleteParticipant(employee)}>удалить</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      <label class="flexbox-horizontal">
        <l1 class="bold-text">Участник</l1>
        <select id="part-select">
          <option value="none" selected disabled hidden>
            Выберете участника
          </option>

          {receivedEmployees.map((employee) => {
            return <option id-key={employee.id}> {employee.name}</option>;
          })}
        </select>
        <button onClick={addEmployeeToMeeting}>Добавить</button>
      </label>

      <label>
        <button onClick={createMeetingQuery}>Сохранить</button>
        <button onClick={() => props.ViewMeetings()}>Не сохранять</button>
      </label>
    </form>
  );
}
export default MeetingEditor;
