pragma solidity ^0.8.0; // Updated to a more recent version of Solidity
pragma experimental ABIEncoderV2;

contract MedicalPlatform {

    struct Patient {
        string name;
        string gender;
        uint age;
        address accountAddress;
        string appointmentData;
        uint[] appointmentNumber;
        uint appointmentCounter;
        string lastDoctor;
    }

    struct Appointment {
        string patientName;
        uint appointmentId;
        string department;
        string doctorName;
        string remark;
        string createTime;
        bool appointStatus;
    }

    struct MedicalRecord {
        uint MedicalRecordId;
        string patientName;
        string hospitalName;
        string department;
        string doctorName;
        string registrationInfo;
        string pastMedicalHistory;
        string currentMedicalHistory;
        bool isFilled;
    }

    struct Doctor {
        string name;
        string gender;
        uint age;
        address accountAddress;
        string MedicalRecordData;
        uint[] MedicalRecordNumber;
        uint MedicalRecordCounter;
    }

    mapping (address => Patient) patients;
    mapping (address => Doctor) doctors;
    mapping (uint => Appointment) public appointments;
    mapping (uint => MedicalRecord) public medicalRecords;

    mapping(address => address[]) public doctorToPatients; 
    mapping(address => uint) public doctorPatientCount; 
    mapping(address => uint[]) public doctorToAppointments;
    mapping (bytes32 => address) public doctorNameToAddress;
    mapping (bytes32 => address) public patientNameToAddress;
    
    uint public appointmentIds = 1;
    uint public patientIds = 1;
    uint public doctorIds = 1;
    uint public medicalRecordIds = 1;

    function keccakString(string memory input) internal pure returns (bytes32) {
        return keccak256(abi.encodePacked(input));
    }

    function registerPatient(string memory _name, string memory _gender, address _address, uint _age) public {
        bytes32 nameHash = keccakString(_name);
        patients[_address] = Patient(_name, _gender, _age, _address, "", new uint[](0), 0, "");
        patientIds += 1;
        patientNameToAddress[nameHash] = _address;
    }

    function registerDoctor(string memory _name, string memory _gender, address _address, uint _age) public {
        bytes32 nameHash = keccakString(_name);
        doctors[_address] = Doctor(_name, _gender, _age, _address, "", new uint[](0), 0);
        doctorIds += 1;
        doctorNameToAddress[nameHash] = _address;
    }

    function getPatientInfo(address _address) public view returns (string memory, string memory, uint, address) {
        Patient storage patient = patients[_address];
        return (patient.name, patient.gender, patient.age, patient.accountAddress);
    }

    function getDoctorInfo(address _address) public view returns (string memory, string memory, uint, address) {
        Doctor storage doctor = doctors[_address];
        return (doctor.name, doctor.gender, doctor.age, doctor.accountAddress);
    }

    function makeAppointment(string memory _patientName, string memory _department, string memory _doctorName, string memory _remark, string memory _createTime) public {
        bytes32 patientNameHash = keccak256(abi.encodePacked(_patientName));
        bytes32 doctorNameHash = keccak256(abi.encodePacked(_doctorName));
        address _patientAddress = patientNameToAddress[patientNameHash];
        address _doctorAddress = doctorNameToAddress[doctorNameHash];
        require(_patientAddress != address(0), "Patient is not registered.");
        require(_doctorAddress != address(0), "Doctor is not registered.");
    
        uint appointmentId = appointmentIds++;
        Appointment memory appoint = Appointment({
            patientName: _patientName,
            appointmentId: appointmentId,
            department: _department,
            doctorName: _doctorName,
            remark: _remark,
            createTime: _createTime,
            appointStatus: false
        });
        appointments[appointmentId] = appoint;
    
        Patient storage patient = patients[_patientAddress];
        patient.appointmentNumber.push(appointmentId);
        patient.appointmentCounter += 1;
        patient.lastDoctor = _doctorName;
    
        bool exists = false;
        for (uint i = 0; i < doctorToPatients[_doctorAddress].length; i++) {
            if (doctorToPatients[_doctorAddress][i] == _patientAddress) {
                exists = true;
                break;
            }
        }
    
        if (!exists) {
            doctorToPatients[_doctorAddress].push(_patientAddress);
            doctorPatientCount[_doctorAddress] += 1;
        }
    
        doctorToAppointments[_doctorAddress].push(appointmentId);
    }

    function getPatientAppointment(address _patientAddress) public view returns (uint[] memory) {
        return patients[_patientAddress].appointmentNumber;
    }

    function getAppointmentById(uint _appointmentId) public view returns (string memory, uint, string memory, string memory, string memory, string memory, bool) {
        Appointment storage app = appointments[_appointmentId];
        return (app.patientName, app.appointmentId, app.department, app.doctorName, app.remark, app.createTime, app.appointStatus);
    }

    function getAppointmentCount(address _patientAddress) public view returns (uint) {
        return patients[_patientAddress].appointmentNumber.length;
    }

    function fillInMedicalHistory(string memory _patientName, string memory _hospitalName, string memory _department, string memory _doctorName, string memory _registrationInfo, string memory _pastMedicalHistory, string memory _currentMedicalHistory) public {
        uint medicalRecordId = medicalRecordIds++;
        bytes32 doctorNameHash = keccak256(abi.encodePacked(_doctorName));
        bytes32 patientNameHash = keccak256(abi.encodePacked(_patientName));
        address _doctorAddress = doctorNameToAddress[doctorNameHash];
        address _patientAddress = patientNameToAddress[patientNameHash];
        MedicalRecord memory medical = MedicalRecord({
            MedicalRecordId: medicalRecordId,
            patientName: _patientName,
            hospitalName: _hospitalName,
            department: _department,
            doctorName: _doctorName,
            registrationInfo: _registrationInfo,
            pastMedicalHistory: _pastMedicalHistory,
            currentMedicalHistory: _currentMedicalHistory,
            isFilled: true
        });
        medicalRecords[medicalRecordId] = medical;
        Doctor storage doctor = doctors[_doctorAddress];
        doctor.MedicalRecordNumber.push(medicalRecordId);
        doctor.MedicalRecordCounter += 1;
        doctor.name = _doctorName;
    }

    function endConsultation(uint medicalRecordId) public {
        require(medicalRecords[medicalRecordId].isFilled, "Medical record must be filled.");
        medicalRecords[medicalRecordId].isFilled = false;
    }

    function getMedicalHistory(uint medicalRecordId) public view returns (uint, string memory, string memory, string memory, string memory, string memory, string memory, string memory) {
        MedicalRecord storage medical = medicalRecords[medicalRecordId];
        require(medical.isFilled, "Medical Record does not exist!");
        return (medical.MedicalRecordId, medical.patientName, medical.hospitalName, medical.department, medical.doctorName, medical.registrationInfo, medical.pastMedicalHistory, medical.currentMedicalHistory);
    }

    function getMedicalHistoryNumber(address doctorAddress) public view returns (uint[] memory) {
        return doctors[doctorAddress].MedicalRecordNumber;
    }

    function getAll(address _doctorAddress) public view returns (uint, uint) {
        uint[] memory appointmentCount = doctorToAppointments[_doctorAddress];
        address[] memory patientCount = doctorToPatients[_doctorAddress];
        return (appointmentCount.length, patientCount.length);
    }

    function getPatientsInfoByDoctor(address _doctorAddress) public view returns (Patient[] memory) {
        address[] memory patientAddresses = doctorToPatients[_doctorAddress];
        Patient[] memory patientsInfo = new Patient[](patientAddresses.length);
        
        for (uint i = 0; i < patientAddresses.length; i++) {
            address patientAddress = patientAddresses[i];
            Patient storage patient = patients[patientAddress];
            patientsInfo[i] = patient;
        }
        
        return patientsInfo;
    }

    function getAppointmentsByDoctor(address _doctorAddress) public view returns (Appointment[] memory) {
        uint[] memory appointmentIds = doctorToAppointments[_doctorAddress];
        Appointment[] memory appointmentsInfo = new Appointment[](appointmentIds.length);
        
        for (uint i = 0; i < appointmentIds.length; i++) {
            uint appointmentId = appointmentIds[i];
            Appointment storage appointment = appointments[appointmentId];
            appointmentsInfo[i] = appointment;
        }
        
        return appointmentsInfo;
    }

    function fillInAppointment(uint appointmentId) public {
        require(appointmentId != 0, "appointmentId is zero");
        appointments[appointmentId].appointStatus = true;
    }
}